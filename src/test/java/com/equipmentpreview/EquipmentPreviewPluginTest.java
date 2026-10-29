package com.equipmentpreview;

import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemStats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EquipmentPreviewPluginTest
{
	@Test
	public void onlyAddsToggleToShowWornItems()
	{
		assertTrue(EquipmentPreviewPlugin.isEquipmentButtonMenu(
			InterfaceID.Bankmain.WORNITEMS_BUTTON, "Show worn items"));
		assertFalse(EquipmentPreviewPlugin.isEquipmentButtonMenu(
			InterfaceID.Bankmain.WORNITEMS_BUTTON, "Deposit worn items"));
		assertFalse(EquipmentPreviewPlugin.isEquipmentButtonMenu(0, "Show worn items"));
	}

	@Test
	public void addsEquipmentBonuses()
	{
		EquipmentTotals totals = new EquipmentTotals();
		totals.add(new ItemStats(true, 0, 0, ItemEquipmentStats.builder()
			.astab(3).dmagic(-2).mdmg(1.5f).prayer(1).build()));
		totals.add(new ItemStats(true, 0, 0, ItemEquipmentStats.builder()
			.astab(4).dmagic(5).mdmg(0.5f).prayer(2).build()));

		assertEquals(7, totals.astab);
		assertEquals(3, totals.dmagic);
		assertEquals(2.0f, totals.mdmg, 0.001f);
		assertEquals(3, totals.prayer);
		assertEquals("+1.5%", EquipmentPreviewOverlay.formatMagicDamage(1.5f));
		assertEquals("-0.5%", EquipmentPreviewOverlay.formatMagicDamage(-0.5f));
	}

	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EquipmentPreviewPlugin.class);
		RuneLite.main(args);
	}
}
