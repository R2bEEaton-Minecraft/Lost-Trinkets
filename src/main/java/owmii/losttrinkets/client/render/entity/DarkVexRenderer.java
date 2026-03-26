package owmii.losttrinkets.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DarkVexRenderer extends VexRenderer {
    public DarkVexRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
