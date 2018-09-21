package com.mewna.catnip.cache;

import com.google.common.collect.ImmutableList;
import com.mewna.catnip.Catnip;
import com.mewna.catnip.entity.*;
import com.mewna.catnip.entity.Emoji.CustomEmoji;
import com.mewna.catnip.entity.impl.EntityBuilder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.mewna.catnip.shard.DiscordEvent.*;

/**
 * @author amy
 * @since 9/18/18.
 */
@Accessors(fluent = true, chain = true)
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class MemoryEntityCache implements EntityCacheWorker {
    // TODO: What even is efficiency
    
    private final Map<String, Guild> guildCache = new ConcurrentHashMap<>();
    private final Map<String, User> userCache = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Member>> memberCache = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Role>> roleCache = new ConcurrentHashMap<>();
    // TODO: How to handle DM channels?
    private final Map<String, Map<String, Channel>> channelCache = new ConcurrentHashMap<>();
    private final Map<String, Map<String, CustomEmoji>> emojiCache = new ConcurrentHashMap<>();
    private final Map<String, Map<String, VoiceState>> voiceStateCache = new ConcurrentHashMap<>();
    private final Map<String, Presence> presenceCache = new ConcurrentHashMap<>();
    @Getter
    private Catnip catnip;
    private EntityBuilder entityBuilder;
    
    @Nonnull
    @CheckReturnValue
    private static <T> Function<JsonArray, List<T>> mapObjectContents(@Nonnull final Function<JsonObject, T> builder) {
        return array -> {
            final Collection<T> result = new ArrayList<>(array.size());
            for(final Object object : array) {
                if(!(object instanceof JsonObject)) {
                    throw new IllegalArgumentException("Expected array to contain only objects, but found " +
                            (object == null ? "null" : object.getClass())
                    );
                }
                result.add(builder.apply((JsonObject) object));
            }
            return ImmutableList.copyOf(result);
        };
    }
    
    private void cacheChannel(final Channel channel) {
        if(channel.isGuild()) {
            final GuildChannel gc = (GuildChannel) channel;
            Map<String, Channel> channels = channelCache.get(gc.guildId());
            if(channels == null) {
                channels = new ConcurrentHashMap<>();
                channelCache.put(gc.guildId(), channels);
            }
            channels.put(gc.id(), gc);
            catnip.logAdapter().debug("Cached channel {} for guild {}", gc.id(), gc.guildId());
        } else {
            catnip.logAdapter().warn("I don't know how to cache non-guild channel {}!", channel.id());
        }
    }
    
    private void cacheRole(final Role role) {
        Map<String, Role> roles = roleCache.get(role.guildId());
        if(roles == null) {
            roles = new ConcurrentHashMap<>();
            roleCache.put(role.guildId(), roles);
        }
        roles.put(role.id(), role);
        catnip.logAdapter().debug("Cached role {} for guild {}", role.id(), role.guildId());
    }
    
    private void cacheUser(final User user) {
        userCache.put(user.id(), user);
        catnip.logAdapter().debug("Cached user {}", user.id());
    }
    
    private void cacheMember(final Member member) {
        Map<String, Member> members = memberCache.get(member.guildId());
        if(members == null) {
            members = new ConcurrentHashMap<>();
            memberCache.put(member.guildId(), members);
        }
        members.put(member.id(), member);
        catnip.logAdapter().debug("Cached member {} for guild {}", member.id(), member.guildId());
    }
    
    private void cacheEmoji(final CustomEmoji emoji) {
        Map<String, CustomEmoji> emojiMap = emojiCache.get(emoji.guildId());
        if(emojiMap == null) {
            emojiMap = new ConcurrentHashMap<>();
            emojiCache.put(emoji.guildId(), emojiMap);
        }
        emojiMap.put(emoji.id(), emoji);
        catnip.logAdapter().debug("Cached member {} for guild {}", emoji.id(), emoji.guildId());
    }
    
    private void cachePresence(final String id, final Presence presence) {
        presenceCache.put(id, presence);
    }
    
    private void cacheVoiceState(final VoiceState state) {
        if(state.guildId() == null) {
            catnip.logAdapter().warn("Not caching voice state for {} due to null guild", state.userId());
            return;
        }
        Map<String, VoiceState> states = voiceStateCache.get(state.guildId());
        if(states == null) {
            states = new ConcurrentHashMap<>();
            voiceStateCache.put(state.guildId(), states);
        }
        states.put(state.userId(), state);
        catnip.logAdapter().debug("Cached voice state for {} in guild {}", state.userId(), state.guildId());
    }
    
    @Nonnull
    @Override
    public EntityCache updateCache(@Nonnull final String eventType, @Nonnull final JsonObject payload) {
        switch(eventType) {
            // Channels
            case CHANNEL_CREATE: {
                final Channel channel = entityBuilder.createChannel(payload);
                cacheChannel(channel);
                break;
            }
            case CHANNEL_UPDATE: {
                final Channel channel = entityBuilder.createChannel(payload);
                cacheChannel(channel);
                break;
            }
            case CHANNEL_DELETE: {
                final Channel channel = entityBuilder.createChannel(payload);
                if(channel.isGuild()) {
                    final GuildChannel gc = (GuildChannel) channel;
                    Map<String, Channel> channels = channelCache.get(gc.guildId());
                    if(channels == null) {
                        channels = new ConcurrentHashMap<>();
                        channelCache.put(gc.guildId(), channels);
                    }
                    channels.remove(gc.id());
                    catnip.logAdapter().debug("Deleted channel {} for guild {}", gc.id(), gc.guildId());
                } else {
                    catnip.logAdapter().warn("I don't know how to delete non-guild channel {}!", channel.id());
                }
                break;
            }
            // Guilds
            case GUILD_CREATE: {
                final Guild guild = entityBuilder.createGuild(payload);
                guildCache.put(guild.id(), guild);
                break;
            }
            case GUILD_UPDATE: {
                final Guild guild = entityBuilder.createGuild(payload);
                guildCache.put(guild.id(), guild);
                break;
            }
            case GUILD_DELETE: {
                final Guild guild = entityBuilder.createGuild(payload);
                guildCache.remove(guild.id());
                break;
            }
            // Roles
            case GUILD_ROLE_CREATE: {
                final String guild = payload.getString("guild_id");
                final JsonObject json = payload.getJsonObject("role");
                final Role role = entityBuilder.createRole(guild, json);
                cacheRole(role);
                break;
            }
            case GUILD_ROLE_UPDATE: {
                final String guild = payload.getString("guild_id");
                final JsonObject json = payload.getJsonObject("role");
                final Role role = entityBuilder.createRole(guild, json);
                cacheRole(role);
                break;
            }
            case GUILD_ROLE_DELETE: {
                final String guild = payload.getString("guild_id");
                final String role = payload.getString("role_id");
                Optional.ofNullable(roleCache.get(guild)).ifPresent(e -> e.remove(role));
                catnip.logAdapter().debug("Deleted role {} for guild {}", role, guild);
                break;
            }
            // Members
            case GUILD_MEMBER_ADD: {
                final Member member = entityBuilder.createMember(payload.getString("guild_id"), payload);
                final User user = entityBuilder.createUser(payload.getJsonObject("user"));
                cacheUser(user);
                cacheMember(member);
                break;
            }
            case GUILD_MEMBER_UPDATE: {
                // This doesn't send an object like all the other events, so we build a fake
                // payload object and create an entity from that
                final JsonObject user = payload.getJsonObject("user");
                final String id = user.getString("id");
                final String guild = payload.getString("guild_id");
                final Member old = member(guild, id);
                if(old != null) {
                    final JsonObject data = new JsonObject()
                            .put("user", user)
                            .put("roles", payload.getJsonArray("roles"))
                            .put("nick", payload.getString("nick"))
                            .put("deaf", old.deaf())
                            .put("mute", old.mute())
                            .put("joined_at", old.joinedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    final Member member = entityBuilder.createMember(guild, data);
                    cacheMember(member);
                } else {
                    catnip.logAdapter().warn("Got GUILD_MEMBER_UPDATE for {} in {}, but we don't have them cached?!", id, guild);
                }
                break;
            }
            case GUILD_MEMBER_REMOVE: {
                final String guild = payload.getString("guild_id");
                final String user = payload.getJsonObject("user").getString("id");
                Optional.ofNullable(memberCache.get(guild)).ifPresent(e -> e.remove(user));
                break;
            }
            // Member chunking
            case GUILD_MEMBERS_CHUNK: {
                final String guild = payload.getString("guild_id");
                final JsonArray members = payload.getJsonArray("members");
                members.stream().map(e -> entityBuilder.createMember(guild, (JsonObject) e)).forEach(this::cacheMember);
                catnip.logAdapter().debug("Processed chunk of {} members for guild {}", members.size(), guild);
                break;
            }
            // Emojis
            case GUILD_EMOJIS_UPDATE: {
                if(!catnip.cacheFlags().contains(CacheFlag.DROP_EMOJI)) {
                    final String guild = payload.getString("guild_id");
                    final JsonArray emojis = payload.getJsonArray("emojis");
                    emojis.stream().map(e -> entityBuilder.createCustomEmoji(guild, (JsonObject) e)).forEach(this::cacheEmoji);
                    catnip.logAdapter().debug("Processed chunk of {} emojis for guild {}", emojis.size(), guild);
                }
                break;
            }
            // Users
            case USER_UPDATE: {
                // TODO: This is self, do we care?
                break;
            }
            case PRESENCE_UPDATE: {
                final JsonObject user = payload.getJsonObject("user");
                final String id = user.getString("id");
                final User old = user(id);
                if(old == null) {
                    catnip.logAdapter().warn("Received PRESENCE_UPDATE for uncached user {}!?", id);
                } else {
                    // This could potentially update:
                    // - username
                    // - discriminator
                    // - avatar
                    // so we check the existing cache for a user, and update as needed
                    final User updated = entityBuilder.createUser(new JsonObject()
                            .put("id", id)
                            .put("bot", old.bot())
                            .put("username", user.getString("username", old.username()))
                            .put("discriminator", user.getString("discriminator", old.discriminator()))
                            .put("avatar", user.getString("avatar", old.avatar()))
                    );
                    cacheUser(updated);
                    if(!catnip.cacheFlags().contains(CacheFlag.DROP_GAME_STATUSES)) {
                        final Presence presence = entityBuilder.createPresence(payload);
                        cachePresence(id, presence);
                    }
                }
                break;
            }
            // Voice
            case VOICE_STATE_UPDATE: {
                if(!catnip.cacheFlags().contains(CacheFlag.DROP_VOICE_STATES)) {
                    final VoiceState state = entityBuilder.createVoiceState(payload);
                    cacheVoiceState(state);
                }
                break;
            }
        }
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheUsers(@Nonnull final Collection<User> users) {
        users.forEach(this::cacheUser);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheChannels(@Nonnull final Collection<GuildChannel> channels) {
        channels.forEach(this::cacheChannel);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheRoles(@Nonnull final Collection<Role> roles) {
        roles.forEach(this::cacheRole);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheMembers(@Nonnull final Collection<Member> members) {
        members.forEach(this::cacheMember);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheEmoji(@Nonnull final Collection<CustomEmoji> emoji) {
        emoji.forEach(this::cacheEmoji);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCachePresences(@Nonnull final Map<String, Presence> presences) {
        presences.forEach(this::cachePresence);
        return this;
    }
    
    @Nonnull
    @Override
    public EntityCache bulkCacheVoiceStates(@Nonnull final Collection<VoiceState> voiceStates) {
        voiceStates.forEach(this::cacheVoiceState);
        return this;
    }
    
    @Nullable
    @Override
    public Guild guild(@Nonnull final String id) {
        return guildCache.get(id);
    }
    
    @Nullable
    @Override
    public User user(@Nonnull final String id) {
        return userCache.get(id);
    }
    
    @Nullable
    @Override
    public Presence presence(@Nonnull final String id) {
        return presenceCache.get(id);
    }
    
    @Nullable
    @Override
    public Member member(@Nonnull final String guildId, @Nonnull final String id) {
        if(memberCache.containsKey(guildId)) {
            return memberCache.get(guildId).get(id);
        } else {
            return null;
        }
    }
    
    @Nonnull
    @Override
    public List<Member> members(@Nonnull final String guildId) {
        if(memberCache.containsKey(guildId)) {
            return ImmutableList.copyOf(memberCache.get(guildId).values());
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nullable
    @Override
    public Role role(@Nonnull final String guildId, @Nonnull final String id) {
        if(roleCache.containsKey(guildId)) {
            return roleCache.get(guildId).get(id);
        } else {
            return null;
        }
    }
    
    @Nonnull
    @Override
    public List<Role> roles(@Nonnull final String guildId) {
        if(roleCache.containsKey(guildId)) {
            return ImmutableList.copyOf(roleCache.get(guildId).values());
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nullable
    @Override
    public Channel channel(@Nonnull final String guildId, @Nonnull final String id) {
        if(channelCache.containsKey(guildId)) {
            return channelCache.get(guildId).get(id);
        } else {
            return null;
        }
    }
    
    @Nonnull
    @Override
    public List<Channel> channels(@Nonnull final String guildId) {
        if(channelCache.containsKey(guildId)) {
            return ImmutableList.copyOf(channelCache.get(guildId).values());
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nullable
    @Override
    public CustomEmoji emoji(@Nonnull final String guildId, @Nonnull final String id) {
        if(emojiCache.containsKey(guildId)) {
            return emojiCache.get(guildId).get(id);
        } else {
            return null;
        }
    }
    
    @Nonnull
    @Override
    public List<CustomEmoji> emojis(@Nonnull final String guildId) {
        if(emojiCache.containsKey(guildId)) {
            return ImmutableList.copyOf(emojiCache.get(guildId).values());
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nullable
    @Override
    public VoiceState voiceState(@Nullable final String guildId, @Nonnull final String id) {
        if(voiceStateCache.containsKey(guildId)) {
            return voiceStateCache.get(guildId).get(id);
        } else {
            return null;
        }
    }
    
    @Nonnull
    @Override
    public List<VoiceState> voiceStates(@Nonnull final String guildId) {
        if(voiceStateCache.containsKey(guildId)) {
            return ImmutableList.copyOf(voiceStateCache.get(guildId).values());
        } else {
            return ImmutableList.of();
        }
    }
    
    @Nonnull
    @Override
    public EntityCache catnip(@Nonnull final Catnip catnip) {
        this.catnip = catnip;
        entityBuilder = new EntityBuilder(catnip);
        return this;
    }
}
