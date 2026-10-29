package com.equipmentpreview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class EquipmentPreviewOverlay extends OverlayPanel
{
	private static final int GAP_X = 6;
	private static final int PANEL_GAP = 6;
	private static final int PANEL_WIDTH = 3 * Constants.ITEM_SPRITE_WIDTH + 2 * GAP_X
		+ 2 * ComponentConstants.STANDARD_BORDER;
	private static final int[] DISPLAY_SLOTS = {
		-1, EquipmentInventorySlot.HEAD.getSlotIdx(), -1,
		EquipmentInventorySlot.CAPE.getSlotIdx(), EquipmentInventorySlot.AMULET.getSlotIdx(), EquipmentInventorySlot.AMMO.getSlotIdx(),
		EquipmentInventorySlot.WEAPON.getSlotIdx(), EquipmentInventorySlot.BODY.getSlotIdx(), EquipmentInventorySlot.SHIELD.getSlotIdx(),
		-1, EquipmentInventorySlot.LEGS.getSlotIdx(), -1,
		EquipmentInventorySlot.GLOVES.getSlotIdx(), EquipmentInventorySlot.BOOTS.getSlotIdx(), EquipmentInventorySlot.RING.getSlotIdx()
	};
	private static final ImageComponent EMPTY_SLOT = new ImageComponent(new BufferedImage(
		Constants.ITEM_SPRITE_WIDTH,
		Constants.ITEM_SPRITE_HEIGHT,
		BufferedImage.TYPE_4BYTE_ABGR));

	private final Client client;
	private final EquipmentPreviewConfig config;
	private final ItemManager itemManager;
	private final PanelComponent itemGrid = new PanelComponent();

	@Inject
	private EquipmentPreviewOverlay(Client client, EquipmentPreviewConfig config, ItemManager itemManager)
	{
		this.client = client;
		this.config = config;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setMovable(true);
		setSnappable(false);
		setResizable(false);

		panelComponent.setBackgroundColor(new Color(70, 61, 50));
		panelComponent.setGap(new Point(0, 2));
		panelComponent.setPreferredSize(new Dimension(PANEL_WIDTH, 0));
		panelComponent.setOrientation(ComponentOrientation.VERTICAL);

		itemGrid.setWrap(true);
		itemGrid.setGap(new Point(GAP_X, 4));
		itemGrid.setBorder(new Rectangle());
		itemGrid.setBackgroundColor(null);
		itemGrid.setOrientation(ComponentOrientation.HORIZONTAL);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Widget bank = client.getWidget(InterfaceID.Bankmain.UNIVERSE);
		if (!config.showPreview() || bank == null || bank.isHidden())
		{
			return null;
		}

		if (getPreferredLocation() == null)
		{
			setPreferredLocation(defaultLocation(bank));
		}

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		if (equipment == null)
		{
			return null;
		}

		EquipmentTotals totals = new EquipmentTotals();
		itemGrid.getChildren().clear();
		for (int slot : DISPLAY_SLOTS)
		{
			Item item = slot < 0 ? null : equipment.getItem(slot);
			itemGrid.getChildren().add(item == null || item.getId() < 0 ? EMPTY_SLOT : image(item));
			if (config.showStats() && item != null && item.getId() >= 0)
			{
				totals.add(itemManager.getItemStats(item.getId()));
			}
		}

		panelComponent.getChildren().add(itemGrid);
		if (config.showStats())
		{
			addStats(totals);
		}
		return super.render(graphics);
	}

	private Point defaultLocation(Widget bank)
	{
		net.runelite.api.Point bankLocation = bank.getCanvasLocation();
		int right = bankLocation.getX() + bank.getWidth() + PANEL_GAP;
		int x = right + PANEL_WIDTH <= client.getCanvasWidth()
			? right
			: bankLocation.getX() - PANEL_WIDTH - PANEL_GAP;
		return new Point(x, bankLocation.getY());
	}

	private ImageComponent image(Item item)
	{
		ItemComposition composition = itemManager.getItemComposition(item.getId());
		BufferedImage image = itemManager.getImage(item.getId(), item.getQuantity(), composition.isStackable());
		return image == null ? EMPTY_SLOT : new ImageComponent(image);
	}

	private void addStats(EquipmentTotals stats)
	{
		addTitle("Attack bonus");
		addStat("Stab", stats.astab);
		addStat("Slash", stats.aslash);
		addStat("Crush", stats.acrush);
		addStat("Magic", stats.amagic);
		addStat("Ranged", stats.arange);
		addTitle("Defence bonus");
		addStat("Stab", stats.dstab);
		addStat("Slash", stats.dslash);
		addStat("Crush", stats.dcrush);
		addStat("Magic", stats.dmagic);
		addStat("Ranged", stats.drange);
		addTitle("Other bonuses");
		addStat("Melee strength", stats.str);
		addStat("Ranged strength", stats.rstr);
		addStat("Magic damage", formatMagicDamage(stats.mdmg), stats.mdmg);
		addStat("Prayer", stats.prayer);
	}

	static String formatMagicDamage(float value)
	{
		return String.format(Locale.ROOT, "%+.1f%%", value);
	}

	private void addTitle(String title)
	{
		panelComponent.getChildren().add(TitleComponent.builder()
			.text(title)
			.color(JagexColors.DARK_ORANGE_INTERFACE_TEXT)
			.build());
	}

	private void addStat(String name, int value)
	{
		addStat(name, String.format("%+d", value), value);
	}

	private void addStat(String name, String value, float number)
	{
		panelComponent.getChildren().add(LineComponent.builder()
			.left(name)
			.right(value)
			.rightColor(number > 0 ? Color.GREEN : number < 0 ? Color.RED : JagexColors.YELLOW_INTERFACE_TEXT)
			.build());
	}
}
