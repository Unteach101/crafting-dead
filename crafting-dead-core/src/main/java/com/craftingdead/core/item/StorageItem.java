/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.core.item;


import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.storage.DefaultStorage;
import com.craftingdead.core.capability.storage.IStorage;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.GenericContainer;
import com.craftingdead.core.util.Text;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public class StorageItem extends Item {

  public static final int MAX_ROWS_TO_SHOW = 6;

  public static final Supplier<IStorage> VEST =
      () -> new DefaultStorage(2 * 9, InventorySlotType.VEST, GenericContainer::createVest);

  private final Supplier<IStorage> storageContainer;

  public StorageItem(Supplier<IStorage> storageContainer, Properties properties) {
    super(properties);
    this.storageContainer = storageContainer;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(this.storageContainer.get(), ImmutableSet
        .of(() -> ModCapabilities.STORAGE, () -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY));
  }

  @Override
  public void addInformation(ItemStack backpackStack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(backpackStack, world, lines, tooltipFlag);

    backpackStack.getCapability(ModCapabilities.STORAGE).ifPresent(storage -> {
      if (!storage.isEmpty()) {
        lines.add(Text.of(" "));
        lines
            .add(Text
                .translate("container.inventory")
                .applyTextStyles(TextFormatting.RED, TextFormatting.BOLD));

        int rowsBeyondLimit = 0;

        for (int i = 0; i < storage.getSlots(); i++) {
          ItemStack stack = storage.getStackInSlot(i);
          if (!stack.isEmpty()) {
            if (i >= MAX_ROWS_TO_SHOW) {
              ++rowsBeyondLimit;
            } else {
              ITextComponent amountText =
                  Text.of(stack.getCount() + "x ").applyTextStyle(TextFormatting.DARK_GRAY);
              ITextComponent itemText = stack.getDisplayName().applyTextStyle(TextFormatting.GRAY);
              lines.add(amountText.appendSibling(itemText));
            }
          }
        }

        if (rowsBeyondLimit > 0) {
          lines.add(Text.of(". . . +" + rowsBeyondLimit).applyTextStyle(TextFormatting.RED));
        }
      }
    });
  }
}
