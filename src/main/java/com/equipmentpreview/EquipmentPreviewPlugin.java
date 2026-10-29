package com.equipmentpreview;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Bank Equipment Preview",
	description = "Shows equipped items beside the bank",
	tags = {"bank", "equipment", "gear", "worn"}
)
public class EquipmentPreviewPlugin extends Plugin
{
	private static final String SHOW_WORN_ITEMS = "Show worn items";
	private static final String TOGGLE_PREVIEW = "Toggle equipment preview";

	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private EquipmentPreviewConfig config;

	@Inject
	private EquipmentPreviewOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Provides
	EquipmentPreviewConfig provideConfig(ConfigManager manager)
	{
		return manager.getConfig(EquipmentPreviewConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!isEquipmentButtonMenu(event.getActionParam1(), event.getOption()))
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption(TOGGLE_PREVIEW)
			.setTarget(event.getTarget())
			.setType(MenuAction.RUNELITE)
			.onClick(entry -> configManager.setConfiguration(
				EquipmentPreviewConfig.GROUP,
				EquipmentPreviewConfig.SHOW_PREVIEW_KEY,
				!config.showPreview()));
	}

	static boolean isEquipmentButtonMenu(int widgetId, String option)
	{
		return widgetId == InterfaceID.Bankmain.WORNITEMS_BUTTON && SHOW_WORN_ITEMS.equals(option);
	}
}
