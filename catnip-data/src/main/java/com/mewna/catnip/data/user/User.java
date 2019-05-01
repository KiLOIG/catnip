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

package com.mewna.catnip.data.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Mentionable;
import com.mewna.catnip.data.Snowflake;
import com.mewna.catnip.util.CDNFormat;
import com.mewna.catnip.util.ImageOptions;
import com.mewna.catnip.util.ImageType;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A single Discord user.
 *
 * @author amy
 * @since 9/4/18
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface User extends Snowflake, Mentionable {
    /**
     * Whether the user's avatar is animated.
     *
     * @return True if the avatar is animated, false otherwise.
     */
    @JsonIgnore
    @CheckReturnValue
    boolean animatedAvatar();
    
    /**
     * The URL for the default avatar for this user.
     *
     * @return String containing the URL to the default avatar. Never null.
     */
    @Nonnull
    @JsonIgnore
    @CheckReturnValue
    default String defaultAvatarUrl() {
        return CDNFormat.defaultAvatarUrl(discriminator());
    }
    
    /**
     * The URL for the user's set avatar. Can be null if the user has not set an avatar.
     *
     * @param options {@link ImageOptions Image Options}.
     *
     * @return String containing the URL to their avatar, options considered. Can be null.
     *
     * @see User#defaultAvatarUrl() Getting the user's default fallback avatar
     * @see User#effectiveAvatarUrl() Getting the user's effective avatar
     */
    @Nullable
    @JsonIgnore
    @CheckReturnValue
    default String avatarUrl(@Nonnull final ImageOptions options) {
        return CDNFormat.avatarUrl(id(), avatar(), options);
    }
    
    /**
     * The URL for the user's set avatar. Can be null if the user has not set an avatar.
     *
     * @return String containing the URL to their avatar. Can be null.
     *
     * @see User#defaultAvatarUrl() Getting the user's default fallback avatar
     * @see User#effectiveAvatarUrl() Getting the user's effective avatar
     */
    @Nullable
    @JsonIgnore
    @CheckReturnValue
    default String avatarUrl() {
        return avatarUrl(defaultOptions());
    }
    
    /**
     * The URL for the user's effective avatar, as displayed in the Discord client.
     * <br>Convenience method for getting the user's default avatar
     * when {@link User#avatarUrl()} is null.
     *
     * @param options {@link ImageOptions Image Options}.
     *
     * @return String containing a URL to their effective avatar, options considered. Never null.
     */
    @Nonnull
    @JsonIgnore
    @CheckReturnValue
    default String effectiveAvatarUrl(@Nonnull final ImageOptions options) {
        return avatar() == null ? defaultAvatarUrl() : Objects.requireNonNull(
                avatarUrl(options),
                "Avatar url is null but avatar hash is present (??)"
        );
    }
    
    /**
     * The URL for the user's effective avatar, as displayed in the Discord client.
     * <br>Convenience method for getting the user's default avatar
     * when {@link User#avatarUrl()} is null.
     *
     * @return String containing a URL to their effective avatar. Never null.
     */
    @Nonnull
    @JsonIgnore
    @CheckReturnValue
    default String effectiveAvatarUrl() {
        return effectiveAvatarUrl(defaultOptions());
    }
    
    /**
     * The username of the user.
     *
     * @return User's name. Never null.
     */
    @Nonnull
    @CheckReturnValue
    String username();
    
    /**
     * Discriminator of the user, used to tell Amy#0001 from Amy#0002.
     *
     * @return 4 digit discriminator as a string. Never null.
     */
    @Nonnull
    @CheckReturnValue
    String discriminator();
    
    /**
     * The DiscordTag of the user, which is the username, an hash, and the discriminator.
     *
     * @return User's DiscordTag. Never null.
     */
    @Nonnull
    @CheckReturnValue
    default String discordTag() {
        return username() + '#' + discriminator();
    }
    
    /**
     * User's avatar hash.
     * <br><b>This does not return their avatar URL nor image directly.</b>
     *
     * @return User's hashed avatar string. Can be null.
     *
     * @see User#avatarUrl() Getting the user's avatar
     */
    @Nullable
    @CheckReturnValue
    String avatar();
    
    /**
     * Whether the user is a bot, or webhook/fake user.
     *
     * @return True if the user is a bot, false if the user is a human.
     */
    @CheckReturnValue
    boolean bot();
    
    @Nonnull
    @CheckReturnValue
    default String asMention() {
        return "<@" + id() + '>';
    }
    
    private ImageOptions defaultOptions() {
        return new ImageOptions().type(animatedAvatar() ? ImageType.GIF : null);
    }
}
