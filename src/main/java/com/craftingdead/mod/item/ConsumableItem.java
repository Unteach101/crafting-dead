package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ConsumableItem extends Item {

  private final List<Pair<Supplier<EffectInstance>, Float>> effects;

  private final UseAction useAction;

  public ConsumableItem(Properties properties) {
    super(properties);
    this.effects = properties.effects;
    this.useAction = properties.useAction;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (this.isFood() && !playerIn.canEat(this.getFood().canEatWhenFull())) {
      return new ActionResult<>(ActionResultType.FAIL, itemStack);
    }
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity livingEntity) {
    if (livingEntity instanceof ServerPlayerEntity) {
      ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) livingEntity;
      CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
      serverplayerentity.addStat(Stats.ITEM_USED.get(this));
    }

    if (!worldIn.isRemote) {
      livingEntity.curePotionEffects(stack);
      for (Pair<Supplier<EffectInstance>, Float> pair : this.effects) {
        if (pair.getLeft() != null && random.nextFloat() < pair.getRight()) {
          EffectInstance effectInstance = pair.getLeft().get();
          if (effectInstance.getPotion().isInstant()) {
            effectInstance
                .getPotion()
                .affectEntity(livingEntity, livingEntity, livingEntity,
                    effectInstance.getAmplifier(), 1.0D);
          } else {
            livingEntity.addPotionEffect(new EffectInstance(effectInstance));
          }
        }
      }
    }

    if (this.isFood()) {
      stack = livingEntity.onFoodEaten(worldIn, stack);
    } else {
      stack.shrink(1);
    }

    if (this.hasContainerItem(stack)) {
      if (stack.isEmpty()) {
        return this.getContainerItem(stack);
      }

      if (livingEntity instanceof PlayerEntity) {
        PlayerEntity player = (PlayerEntity) livingEntity;
        if (!player.inventory.addItemStackToInventory(this.getContainerItem(stack))) {
          player.dropItem(this.getContainerItem(stack), false);
        }
      }
    }

    return stack;
  }

  @Override
  public UseAction getUseAction(ItemStack itemStack) {
    return this.useAction;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    if (stack.getItem().isFood()) {
      return super.getUseDuration(stack);
    } else {
      return 32;
    }
  }

  public static class Properties extends Item.Properties {

    private final List<Pair<Supplier<EffectInstance>, Float>> effects = new ArrayList<>();

    private UseAction useAction = UseAction.NONE;

    public Properties effect(Supplier<EffectInstance> effect, float probability) {
      this.effects.add(Pair.of(effect, probability));
      return this;
    }

    public Properties setUseAction(UseAction useAction) {
      this.useAction = useAction;
      return this;
    }
  }
}
