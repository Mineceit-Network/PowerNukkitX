package cn.nukkit.network.session;

import cn.nukkit.Player;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.protocol.DataPacket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;


public interface NetworkPlayerSession {
    void sendPacket(DataPacket packet);

    void sendImmediatePacket(DataPacket packet, Runnable callback);

    void sendImmediatePacket(DataPacket packet);

    void disconnect(String reason);

    Player getPlayer();

    void setCompression(CompressionProvider compression);

    CompressionProvider getCompression();

    default void setEncryption(SecretKey agreedKey, Cipher encryptionCipher, Cipher decryptionCipher) {

    }
}
