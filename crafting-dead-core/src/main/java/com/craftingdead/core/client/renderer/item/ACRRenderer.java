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
package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS1;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS2;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ACRRenderer extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelM4A1IS2();

  public ACRRenderer() {
    super(ModItems.ACR);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77.0F));

    matrixStack.translate(0.6F, -0.4F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15.0F));
    matrixStack.translate(-0.2F, 0.55F, 0.0F);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = 0F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 1.2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-33.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.0F));
    matrixStack.translate(0.6F, -0F, 0.25F);

    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-34.5F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.0F));
    matrixStack.translate(1F, -0.23F, 0.94F);

    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(10.0F));

    if (gun.getAttachments().contains(ModItems.RED_DOT_SIGHT.get())) {
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(0.45F));
      matrixStack.translate(0F, 0.015F, 0F);
    }

    matrixStack.rotate(Vector3f.XP.rotationDegrees(-0.4F));
    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.05F, 0F);
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  @Override
  protected void applyWearingTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));

    float scale = 0.55F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.7F, 0.6F, 0.3F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(0.9F, -0.7F, -0.145F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.5F, -0.04F, -0.00F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(1.09F, -0.41F, 0.0625F);
      float scale = 0.25F;
      matrixStack.scale(scale, scale, scale);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is2.png")));
      this.ironSight2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {

    if (itemStack.getItem() == ModItems.STANAG_20_ROUND_MAGAZINE.get()) {
      matrixStack.translate(0.8D, -1.5D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemStack.getItem() == ModItems.STANAG_30_ROUND_MAGAZINE.get()) {
      matrixStack.translate(0.8D, 0D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemStack.getItem() == ModItems.STANAG_DRUM_MAGAZINE.get()) {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
      matrixStack.translate(-1.7D, -0D, 0.5D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.rotate(Vector3f.XP.rotationDegrees(5.0F));
    }

    if (itemStack.getItem() == ModItems.STANAG_BOX_MAGAZINE.get()) {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
      matrixStack.translate(-2.4D, 1.2D, 0.5D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {

    if (attachmentItem == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(-3D, -5D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(-3D, -5D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-3D, -4D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(20D, -2.70D, 1.5D);
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, -1.1D, 1.5D);
      float scale = 1.1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.EOTECH_SIGHT.get()) {
      matrixStack.translate(-1.2D, -4.9D, 1D);
      float scale = 0.12F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.03F, 0.15F, -0.05F);
    }
  }
}
