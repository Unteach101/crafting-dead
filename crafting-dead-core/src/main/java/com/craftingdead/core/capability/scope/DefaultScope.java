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
package com.craftingdead.core.capability.scope;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DefaultScope implements IScope {

  private final float zoomMultiplier;
  private final Optional<ResourceLocation> overlayTexture;
  private final int overlayTextureWidth;
  private final int overlayTextureHeight;

  public DefaultScope() {
    this(1.0F, null, 0, 0);
  }

  public DefaultScope(float zoomMultiplier, ResourceLocation overlayTexture,
      int overlayTextureWidth, int overlayTextureHeight) {
    this.zoomMultiplier = zoomMultiplier;
    this.overlayTexture = Optional.ofNullable(overlayTexture);
    this.overlayTextureWidth = overlayTextureWidth;
    this.overlayTextureHeight = overlayTextureHeight;
  }

  @Override
  public boolean isAiming(Entity entity, ItemStack itemStack) {
    return entity instanceof LivingEntity
        ? ((LivingEntity) entity).getActiveItemStack() == itemStack
        : false;
  }

  @Override
  public float getZoomMultiplier(Entity entity, ItemStack itemStack) {
    return this.zoomMultiplier;
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack) {
    return this.overlayTexture;
  }

  @Override
  public int getOverlayTextureWidth() {
    return this.overlayTextureWidth;
  }

  @Override
  public int getOverlayTextureHeight() {
    return this.overlayTextureHeight;
  }
}
