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

public class ConfigValueS2C {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "config_value_s2c");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        String key = buf.readString(32767);
        String value = buf.readString(32767);
        context.getTaskQueue().execute(() -> ClientInitializer.getConfig().getProperties().setProperty(key, value));
    }

    public static void send(PlayerEntity player, String key, String value) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(key);
        buf.writeString(value);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf);
    }

    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, ConfigValueS2C::accept);
    }
}