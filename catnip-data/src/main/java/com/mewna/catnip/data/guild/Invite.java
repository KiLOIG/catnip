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

package com.mewna.catnip.data.guild;

import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Entity;
import com.mewna.catnip.data.Snowflake;
import com.mewna.catnip.data.channel.Channel.ChannelType;
import com.mewna.catnip.data.guild.Guild.VerificationLevel;
import com.mewna.catnip.util.CDNFormat;
import com.mewna.catnip.util.ImageOptions;
import com.mewna.catnip.util.ImageType;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * An invite to a guild.
 *
 * @author natanbc
 * @since 9/14/18
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface Invite extends Entity {
    /**
     * @return The code for this invite.
     */
    @Nonnull
    @CheckReturnValue
    String code();
    
    /**
     * @return The person who created the invite.
     */
    @Nonnull
    @CheckReturnValue
    Inviter inviter();
    
    /**
     * @return The guild the invite is for.
     */
    @Nonnull
    @CheckReturnValue
    InviteGuild guild();
    
    /**
     * @return The channel the member is for.
     */
    @Nonnull
    @CheckReturnValue
    InviteChannel channel();
    
    /**
     * @return The approximate number of people online in the guild.
     */
    @Nonnegative
    int approximatePresenceCount();
    
    /**
     * @return The approximate number of people in the guild.
     */
    @Nonnegative
    int approximateMemberCount();
    
    @Modifiable
    @CatnipEntity
    interface Inviter extends Snowflake {
        @Nonnull
        @CheckReturnValue
        String username();
        
        @Nonnull
        @CheckReturnValue
        String discriminator();
        
        @Nullable
        @CheckReturnValue
        String avatar();
        
        @CheckReturnValue
        boolean animatedAvatar();
        
        @Nonnull
        @CheckReturnValue
        default String defaultAvatarUrl() {
            return CDNFormat.defaultAvatarUrl(discriminator());
        }
        
        @Nullable
        @CheckReturnValue
        default String avatarUrl(@Nonnull final ImageOptions options) {
            return CDNFormat.avatarUrl(id(), avatar(), options);
        }
        
        @Nullable
        @CheckReturnValue
        default String avatarUrl() {
            return avatarUrl(defaultOptions());
        }
        
        @Nonnull
        @CheckReturnValue
        default String effectiveAvatarUrl(@Nonnull final ImageOptions options) {
            return avatar() == null ? defaultAvatarUrl() :
                    Objects.requireNonNull(avatarUrl(options),
                            "Avatar url is null but avatar hash is present (??)");
        }
        
        @Nonnull
        @CheckReturnValue
        default String effectiveAvatarUrl() {
            return effectiveAvatarUrl(defaultOptions());
        }
        
        private ImageOptions defaultOptions() {
            return new ImageOptions().type(animatedAvatar() ? ImageType.GIF : null);
        }
    }
    
    @Modifiable
    @CatnipEntity
    interface InviteGuild extends Snowflake {
        @Nonnull
        @CheckReturnValue
        String name();
        
        @Nullable
        @CheckReturnValue
        String icon();
        
        @Nullable
        @CheckReturnValue
        String splash();
        
        @Nonnull
        @CheckReturnValue
        List<String> features();
        
        @Nonnull
        @CheckReturnValue
        VerificationLevel verificationLevel();
        
        @Nullable
        @CheckReturnValue
        default String iconUrl(@Nonnull final ImageOptions options) {
            return CDNFormat.iconUrl(id(), icon(), options);
        }
        
        @Nullable
        @CheckReturnValue
        default String iconUrl() {
            return iconUrl(new ImageOptions());
        }
        
        @Nullable
        @CheckReturnValue
        default String splashUrl(@Nonnull final ImageOptions options) {
            return CDNFormat.splashUrl(id(), splash(), options);
        }
        
        @Nullable
        @CheckReturnValue
        default String splashUrl() {
            return splashUrl(new ImageOptions());
        }
    }
    
    @Modifiable
    @CatnipEntity
    interface InviteChannel extends Snowflake {
        @Nonnull
        @CheckReturnValue
        String name();
        
        @Nonnull
        @CheckReturnValue
        ChannelType type();
    }
}
