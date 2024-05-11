package foundationgames.blasttravel;

import foundationgames.blasttravel.entity.CannonEntity;
import foundationgames.blasttravel.item.CannonItem;
import foundationgames.blasttravel.screen.CannonScreenHandler;
import foundationgames.blasttravel.util.BTNetworking;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.quiltmc.qsl.entity.networking.api.tracked_data.QuiltTrackedDataHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlastTravel implements ModInitializer {
	public static final String MOD_ID = "blasttravel";
	public static final Logger LOG = LoggerFactory.getLogger("Blast Travel");

	public static final EntityType<CannonEntity> CANNON = Registry.register(Registries.ENTITY_TYPE, id("cannon"),
			QuiltEntityTypeBuilder.<CannonEntity>create()
					.entityFactory(CannonEntity::new)
					.setDimensions(EntityDimensions.fixed(1, 0.8f))
					.build());

	public static final ScreenHandlerType<CannonScreenHandler> CANNON_SCREEN_HANDLER = Registry.register(
		Registries.SCREEN_HANDLER_TYPE, id("cannon"), new ScreenHandlerType<>(CannonScreenHandler::new, FeatureFlags.DEFAULT_SET));

	public static final Item CANNON_ITEM = Registry.register(Registries.ITEM, id("cannon"),
			new CannonItem(new Item.Settings()));

	public static final DefaultParticleType CANNON_BLAST = Registry.register(Registries.PARTICLE_TYPE,
			id("cannon_blast"), FabricParticleTypes.simple(true));

	public static final TrackedDataHandler<ItemStack> ITEM_STACK_HANDLER = TrackedDataHandler.create(PacketByteBuf::writeItemStack, PacketByteBuf::readItemStack);

	@Override
	public void onInitialize(ModContainer mod) {
		BTNetworking.init();

		QuiltTrackedDataHandlerRegistry.register(id("item_stack_handler"), ITEM_STACK_HANDLER);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL_BLOCKS).register(entries -> entries.addItem(CANNON_ITEM));
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
