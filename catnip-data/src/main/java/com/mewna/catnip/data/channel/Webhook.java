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

import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Snowflake;
import com.mewna.catnip.data.guild.GuildEntity;
import com.mewna.catnip.data.user.User;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A webhook on a channel. Allows sending messages to a text channel in a guild
 * without having to have a bot application.
 *
 * @author natanbc
 * @since 9/15/18
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface Webhook extends GuildEntity, Snowflake {
    /**
     * @return The id of the channel this webhook is for.
     */
    @Nonnull
    @CheckReturnValue
    default String channelId() {
        return Long.toUnsignedString(channelIdAsLong());
    }
    
    /**
     * @return The id of the channel this webhook is for.
     */
    @CheckReturnValue
    long channelIdAsLong();
    
    /**
     * @return The user that created this webhook.
     */
    @Nonnull
    @CheckReturnValue
    User user();
    
    /**
     * @return The name of this webhook.
     */
    @Nullable
    @CheckReturnValue
    String name();
    
    /**
     * @return The default avatar of the webhook.
     */
    @Nullable
    @CheckReturnValue
    String avatar();
    
    /**
     * @return The secure token of the webhook.
     */
    @Nonnull
    @CheckReturnValue
    String token();
}
