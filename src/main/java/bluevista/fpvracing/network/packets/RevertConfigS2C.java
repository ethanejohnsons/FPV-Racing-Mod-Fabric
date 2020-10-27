package bluevista.fpvracing.network.packets;

import bluevista.fpvracing.client.ClientInitializer;
import bluevista.fpvracing.server.ServerInitializer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RevertConfigS2C {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "revert_config_s2c");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        context.getTaskQueue().execute(() -> {
            ClientInitializer.getConfig().readConfig(ClientInitializer.CONFIG_NAME);
            ClientInitializer.physicsWorld.readFromConfig();
            ConfigC2S.send(ClientInitializer.getConfig());
        });
    }

    public static void send(PlayerEntity player) {
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, new PacketByteBuf(Unpooled.buffer()));
    }

    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, RevertConfigS2C::accept);
    }
}