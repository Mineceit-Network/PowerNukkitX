/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
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

package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author joserobjr
 * @since 2021-02-14
 */

@ToString
public class FilterTextPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.FILTER_TEXT_PACKET;
    public String text;
    public boolean fromServer;

    public FilterTextPacket() {
    }


    public FilterTextPacket(String text, boolean fromServer) {
        this.text = text;
        this.fromServer = fromServer;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void encode() {
        reset();
        putString(text);
        putBoolean(fromServer);
    }

    @Override
    public void decode() {
        text = getString();
        fromServer = getBoolean();
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public boolean isFromServer() {
        return fromServer;
    }


    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }
}
