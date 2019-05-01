/*
 * Copyright (c) 2019 amy, All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mewna.catnip.data.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Mentionable;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A channel in a guild that can have text messages sent in it.
 *
 * @author natanbc
 * @since 9/12/18
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface TextChannel extends GuildChannel, MessageChannel, Mentionable {
    /**
     * The channel's topic. Shown at the top of the channel. May be
     * {@code null}.
     *
     * @return The channel's topic.
     */
    @Nullable
    @CheckReturnValue
    String topic();
    
    /**
     * @return Whether or not this channel has been marked as nsfw.
     */
    @CheckReturnValue
    boolean nsfw();
    
    /**
     * The slowmode set on this channel. A value of 0 means no slowmode. Bots
     * are not affected by slowmode.
     *
     * @return The slowmode set on this channel, in seconds.
     */
    @CheckReturnValue
    @Nonnegative
    int rateLimitPerUser();
    
    @Override
    @JsonIgnore
    @CheckReturnValue
    default boolean isText() {
        return type() == ChannelType.TEXT;
    }
    
    @Override
    @JsonIgnore
    @CheckReturnValue
    default boolean isNews() {
        return type() == ChannelType.NEWS;
    }
    
    @Override
    @JsonIgnore
    @CheckReturnValue
    default boolean isVoice() {
        return false;
    }
    
    @Override
    @JsonIgnore
    @CheckReturnValue
    default boolean isCategory() {
        return false;
    }
    
    /**
     * @return A mention for this channel that can be sent in a message.
     */
    @Nonnull
    @JsonIgnore
    @CheckReturnValue
    default String asMention() {
        return "<#" + id() + '>';
    }
}
