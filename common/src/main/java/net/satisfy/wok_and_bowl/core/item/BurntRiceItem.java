package net.satisfy.wok_and_bowl.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.satisfy.wok_and_bowl.core.registry.DataComponentRegistry;

import java.util.List;

public class BurntRiceItem extends Item {
    public BurntRiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String cook = stack.get(DataComponentRegistry.BURNT_BY.get());
        if (cook == null) {
            tooltip.add(Component.translatable("tooltip.wok_and_bowl.burnt_rice.someone").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.wok_and_bowl.burnt_rice.player", Component.literal(cook).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
