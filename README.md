# catnip v2 branch

This branch takes over where the `fuck-vertx` branch left off.

This is, obviously, very much a WIP. Don't expect stuff to work or even necessarily make sense.
The point of this branch is to try to move towards a fairly-explicit set of goals for how 
catnip should be "shaped." catnip v1 already allows for "modular" usage; what I'm aiming
for here is something that allows modular usage and only using the dependencies you need,
while still giving you a single interface to use everything (ie. like how it is now).

### What is this not?

A full rewrite of the lib.

### What is this, then?

Using the base that we already have to make a better lib. Never stop improving and all that.

## TODO list

Adapted list of stuff from the one Discord conversation that I wanna try to get done:

- Java 11+ only
- Get rid of vert.x
- Entity data is a separate module, immutables-only
- No helper/utility/convenience methods on entity data package - that has to be a separate thing.
- Core uses `reactive-streams` only for exposting the external interface - no more `Catnip#on` or similar
- Separate modules that drag in Akka or Project Reactor or RxJava2 or whatever the fuck else you want
- Ratelimiting is just backpressured streams, not buckets like we have now. Would depend on how well this allows sharing ratelimit data tho
- Make it possible to plug in your own gateway handling stuff so you can ex. write in ETF or zstd-stream support as an extension
- Properly submodule out gateway / REST handling
- Possibly make a "blessed" command-handling module
- ...

## What are all these modules?????????

Module name          | Description
---------------------|--------------------------------------------------
`catnip-metadata`    | Metadata about catnip, like version number.
`catnip-utils`       | Utility classes used throughout catnip.
`catnip-data`        | A data-only Discord entity package.
`catnip-entity-spec` | The "spec" that all more-than-data entities must follow. Basically just lays out what convenience methods your entity package should implement. If you wanna bring your own entity classes for some reason, you should implement the stuff in this module.
`catnip-entities`    | The "built-in" implementation of `catnip-entity-spec`. Works off of `catnip-data`.
`catnip-rest`        | The "spec" for how REST implementations work. Implements the majority of the logic around REST requesting, ratelimiting, etc. Calls out to an implementation "package" where needed.
`catnip-gateway`     | Like `catnip-rest` but for the gateway.

### Modules and other stuff that hasn't been decided on yet

- The "default" / "blessed" reactive-streams implementation to use as the default for
  `catnip-rest`/`catnip-gateway` implementations. I wanna say just RxJava2
  (`catnip-gateway-rx`/`catnip-rest-rx`) but open to suggestions. My main concern with
  this is that it would be nice to be able to be like... Pick an implementation and be
  able to just `ReactiveWhatever thing = catnip.rest().whatever();`, ie. returning an
  `Observable` or `Flowable` for Rx, a `Single` or `Flux` or whatever else for Project
  Reactor, ... Not sure how possible this is to do, sadly...
- Event loops? :blobcatsweats: Kind of a big issue
- How to provide deeper hooks / more extension use-cases in general?
- Should we bake in a commands module? If yes, how should it behave?
- How I'm gonna keep my sanity through this whole thing...

# catnip

[![CircleCI](https://circleci.com/gh/mewna/catnip.svg?style=svg)](https://circleci.com/gh/mewna/catnip)
[![powered by potato](https://img.shields.io/badge/powered%20by-potato-%23db325c.svg)](https://mewna.com/)
![GitHub tag (latest by date)](https://img.shields.io/github/tag-date/mewna/catnip.svg?style=popout)


A Discord API wrapper in Java. Fully async / reactive, built on top of
[vert.x](https://vertx.io). catnip tries to map roughly 1:1 to how the Discord 
API works, both in terms of events and REST methods available.

catnip is part of the [amyware Discord server](https://discord.gg/yeF2HpP)

Licensed under the [BSD 3-Clause License](https://tldrlegal.com/license/bsd-3-clause-license-(revised)).

## Installation

[Get it on Jitpack](https://jitpack.io/#com.mewna/catnip)

Current version: ![GitHub tag (latest by date)](https://img.shields.io/github/tag-date/mewna/catnip.svg?style=popout)

### Can I just download a JAR directly?

No. Use a real build tool like [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).

### Javadocs?

[Get them here.](https://mewna.github.io/catnip/docs)

## Features

- Automatic sharding
- Very customizable - you can write [extensions](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/extension/Extension.java)
  for the library, as well as [options](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/CatnipOptions.java)
  for most anything you could want to change. See `EXTENSIONS.md` for more.
- Modular - REST / shards can be used independently. See `MODULAR_USAGE.md` for more.
- Customizable caching - Can run with [no cache](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/cache/NoopEntityCache.java),
  [partial caching](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/cache/CacheFlag.java),
  or [write your own cache handler](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/cache/EntityCacheWorker.java).
  See `CACHING.md` for more.
- Asynchronous cache accesses.
- You can disable individual events.
- You can disable all events, and handle gateway events directly.
- Customizable ratelimit/session data handling - wanna store your 
  [sessions/seqnums](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/shard/session/SessionManager.java) 
  and [REST ratelimit data](https://github.com/mewna/catnip/tree/master/src/main/java/com/mewna/catnip/rest/ratelimit)
  in Redis, but [gateway ratelimits](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/shard/ratelimit/Ratelimiter.java)
  in memory? You can do that!
- [Customizable shard management](https://github.com/mewna/catnip/blob/master/src/main/java/com/mewna/catnip/shard/manager/ShardManager.java)

## Basic usage

This is the simplest possible bot you can make right now:

```Java
final Catnip catnip = Catnip.catnip("your token goes here");
catnip.on(DiscordEvent.MESSAGE_CREATE, msg -> {
    if(msg.content().startsWith("!ping")) {
        msg.channel().sendMessage("pong!");
    }
});
catnip.connect();
```

catnip returns `CompletionStage`s from all REST methods. For example,
editing your ping message to include time it took to create the
message:

```Java
final Catnip catnip = Catnip.catnip("your token goes here");
catnip.on(DiscordEvent.MESSAGE_CREATE, msg -> {
    if(msg.content().equalsIgnoreCase("!ping")) {
        final long start = System.currentTimeMillis();
        msg.channel().sendMessage("pong!")
                .thenAccept(ping -> {
                    final long end = System.currentTimeMillis();
                    ping.edit("pong! (took " + (end - start) + "ms)");
                });
    }
});
catnip.connect();
```

You can also create a catnip instance asynchronously:

```Java
Catnip.catnipAsync("your token here").thenAccept(catnip -> {
    catnip.on(DiscordEvent.MESSAGE_CREATE, msg -> {
        if(msg.content().startsWith("!ping")) {
            msg.channel().sendMessage("pong!");
        }
    });
    catnip.connect();
});
```

Also check out the [examples](https://github.com/mewna/catnip/tree/master/src/main/example/basic) for Kotlin and Scala usage.

### Modular usage

catnip supports being used in REST-only or shards-only configurations. The nice thing about catnip
is that using it like this is **exactly the same** as using it normally. The only difference is
that to use catnip in REST-only mode, you don't call `catnip.connect()` and use 
`catnip.rest().whatever()` instead. 

### Custom event bus events

Because vert.x is intended to be used in clustered mode as well as in a single-node configuration,
emitting events over the built-in event bus requires registering a codec for the events that you
want to fire. If you have an event class `MyEvent`, you can just do something to the effect of
```Java
eventBus().registerDefaultCodec(MyEvent.class, new JsonPojoCodec<>(this, MyEvent.class));
```
where `JsonPojoCodec` is `com.mewna.catnip.util.JsonPojoCodec` and is safe to use.

## Useful extensions

- `catnip-voice` - Voice support for your catnip bot. 
  https://github.com/natanbc/catnip-voice
- `catnip-utilities` - Some extensions for typesafe commands, event waiters, reaction menus, 
  and more. https://github.com/queer/catnip-utilities 

## Why write a fourth Java lib?

- JDA is very nice, but doesn't allow for as much freedom with customizing the internals;
  it's more / less "do it this way or use another lib" in my experience.
- I didn't want ten billion events for every possible case. catnip maps more/less 1:1 with the
  Discord API, and any "extra" events on top of that need to be user-provided via extensions or
  other means. I guess really I just didn't want my lib to be as "high-level" as other libs are.
- I wanted to try to maximize extensibility / customizability, beyond just making it modular. Things
  like being able to intercept raw websocket messages (as JSON), write custom distributed cache handlers,
  ... are incredibly useful.
- I like everything returning `CompletionStage`s instead of custom classes. I do get why other libs
  have them, I just wanted to not.
- I wanted modular usage to be exactly the same more / less no matter what; everything
  should be doable through the catnip instance that you create.
- I wanted to make a lib built on vert.x.
- To take over the world and convert all Java bots. :^)

### Why vert.x?

- vert.x is nice and reactive and async. :tm:
- You can use callbacks or Rx (like we do), but vert.x also provides support for reactive streams
  and kotlin coroutines.
- There's a *lot* of [vert.x libraries and documentation](https://vertx.io/docs/) for just about
  anything you want.
- The reactive, event-loop-driven model fits well for a Discord bot use-case.
