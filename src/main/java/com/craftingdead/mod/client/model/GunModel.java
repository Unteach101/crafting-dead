package com.craftingdead.mod.client.model;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class GunModel implements IBakedModel {

	protected IBakedModel attachmentModel;
	protected IModel attachment;

	public GunModel() {
		try {
			attachment = ModelLoaderRegistry
					.getModel(new ModelResourceLocation(CraftingDead.MOD_ID + ":" + "attachment"));
		} catch (Exception e) {
			CraftingDead.LOGGER.error(e.getMessage());
		}

		if (attachment != null) {
			Function<ResourceLocation, TextureAtlasSprite> textureGetter = resourceLocation -> Minecraft.getMinecraft()
					.getTextureMapBlocks().getAtlasSprite(resourceLocation.toString());

			this.attachmentModel = attachment.bake(attachment.getDefaultState(), new VertexFormat(), textureGetter);
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return attachmentModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return null;
	}
}