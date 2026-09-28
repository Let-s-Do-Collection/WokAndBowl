package net.satisfy.wokandbowl.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.satisfy.wokandbowl.WokAndBowl;

public class WokModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WokAndBowl.identifier("wok"), "main");
    private final ModelPart wok;
    private final ModelPart steamer;

    public WokModel(ModelPart root) {
        this.wok = root.getChild("wok");
        this.steamer = root.getChild("steamer");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("wok", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 9.0F, -2.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(1.0F, 6.0F, -3.0F, 2.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).mirror().addBox(-9.0F, 6.0F, -3.0F, 2.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 11).addBox(-7.0F, 6.0F, -3.0F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 11).mirror().addBox(-7.0F, 6.0F, 7.0F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 42).mirror().addBox(3.0F, 6.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 42).addBox(-11.0F, 6.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, -3.0F));

        partdefinition.addOrReplaceChild("steamer", CubeListBuilder.create().texOffs(0, 11).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderWok(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        this.wok.render(poseStack, vertexConsumer, light, overlay);
    }

    public void renderSteamer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        this.steamer.render(poseStack, vertexConsumer, light, overlay);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        this.wok.render(poseStack, vertexConsumer, light, overlay, color);
        this.steamer.render(poseStack, vertexConsumer, light, overlay, color);
    }
}
