package cn.nukkit.network.protocol.v582;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import lombok.ToString;

@Deprecated(since = "1.20.0-r1")
@ToString
public class EmotePacket_v582 extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.EMOTE_PACKET;


    public long runtimeId;

    public String emoteID;

    public byte flags;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.runtimeId = this.getEntityRuntimeId();
        this.emoteID = this.getString();
        this.flags = (byte) this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.runtimeId);
        this.putString(this.emoteID);
        this.putByte(flags);
    }
}
