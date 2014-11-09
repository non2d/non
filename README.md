```
   __                     __   
  / /  _ __   ___  _ __   \ \  
 | |  | '_ \ / _ \| '_ \   | | 
< <   | | | | (_) | | | |   > >
 | |  |_| |_|\___/|_| |_|  | | 
  \_\    game framework   /_/  
  
```

non is experimental game framework created for rapid game development and so it is perfect for game jams, but also for serious projects. non can run on almost any platform, including Windows, Mac and Linux. I am working on support for Android, web and IOS.

## Features

* Super simple image and text rendering and manipulation
* TMX map rendering
* Audio engine
* Input engine
* Modules system (check below example configuration)
* Game packages (supports classic game directory or zipped game data)
* Simple TCP networking

## Supported languages

* JavaScript `.js`
* [CoffeeScript](http://coffeescript.org/) `.coffee`
* [TypeScript](http://typescriptlang.org/) `.ts`
* [Lua](http://lua.org/) `.lua`
* [Ruby](https://ruby-lang.org) `.rb` (experimental)
* [Python](https://python.org/) `.py` (experimental)
* [Groovy](http://groovy-lang.org/) `.groovy` (experimental)

## Configuration

Configuration is done by json configuration file `non.cfg`. You can define window title, resolution, main class and add modules through it. Example configuration file:
```json
{
    title: "No Nonsense Game",
    resolution: [ 800, 600 ],
    main: "main.js",
    modules: [ audio, graphics, keyboard, mouse, touch, math, tiled, network ]
}
```

## Main class

Your game logic should be divided into 3 events:
* `non.ready` - Initialization logic, loading of game assets
* `non.update` - Update logic, handling input and timed events
* `non.draw` - Display logic, handling rendering of loaded assets

Additional usefull events
* `non.close` - Triggered on game close, unloading of game assets
* `non.resize` - Triggered on game window resize

## Examples

Drawing things:
```
non.ready =>
	image = non.graphics.newImage("image.png")
	tiledMap = non.tiled.newMap("map.tmx")

non.draw =>
	non.graphics.draw(tiledMap)
	non.graphics.draw(image, 0, 0)
	non.graphics.draw("Hello World!", 0, 0)
```

Playing music
```
non.ready =>
	music = non.audio.newMusic("music.ogg")
	non.audio.play(music)
```

Client - Server connection
```
non.ready =>
	non.network.connected (connection) => ...
	non.network.disconnected (connection) => ...
	non.network.received(data, connection) => ...
	non.network.setPort(1111).setHost("localhost").init()
	server = non.network.newServer()
	server.listen()
	client = non.network.newClient()
	client.connect()
```