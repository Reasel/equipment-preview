package com.equipmentpreview;

import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemStats;

class EquipmentTotals
{
	int astab;
	int aslash;
	int acrush;
	int amagic;
	int arange;
	int dstab;
	int dslash;
	int dcrush;
	int dmagic;
	int drange;
	int str;
	int rstr;
	float mdmg;
	int prayer;

	void add(ItemStats stats)
	{
		ItemEquipmentStats equipment = stats == null ? null : stats.getEquipment();
		if (equipment == null)
		{
			return;
		}

		astab += equipment.getAstab();
		aslash += equipment.getAslash();
		acrush += equipment.getAcrush();
		amagic += equipment.getAmagic();
		arange += equipment.getArange();
		dstab += equipment.getDstab();
		dslash += equipment.getDslash();
		dcrush += equipment.getDcrush();
		dmagic += equipment.getDmagic();
		drange += equipment.getDrange();
		str += equipment.getStr();
		rstr += equipment.getRstr();
		mdmg += equipment.getMdmg();
		prayer += equipment.getPrayer();
	}
}
