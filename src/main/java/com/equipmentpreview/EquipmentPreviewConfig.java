package com.equipmentpreview;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(EquipmentPreviewConfig.GROUP)
public interface EquipmentPreviewConfig extends Config
{
	String GROUP = "equipmentPreview";
	String SHOW_PREVIEW_KEY = "showPreview";

	@ConfigItem(
		keyName = SHOW_PREVIEW_KEY,
		name = "Show equipment preview",
		description = "Show equipped items beside the bank",
		position = 0
	)
	default boolean showPreview()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showStats",
		name = "Show equipment stats",
		description = "Show equipment bonuses below the gear preview",
		position = 1
	)
	default boolean showStats()
	{
		return false;
	}
}
