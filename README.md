### *This application is still WIP and will be improved, but it's already fully usable*

# Plugpack
This JavaFX application allows you to easily create Plugin-packs to use as a "Modpack" with [itzg's minecraft server docker container](https://github.com/itzg/docker-minecraft-server).

## Why would you want this?
I know that the docker image lets you directly and automatically download plugins from various sources but the advantage of creating a plugin pack beforehand and then only specifying that as a modpack is, that you can test a pack before putting it on your production servers. I've already had a ton of issues with faulty behavior after updating a plugin (ranging from just that plugin not working to all of my MySQL connections breaking).

## How does it work?
After you specify all your plugins and other options, this application creates a Shell-script that you can easily execute on your Linux server. It uses the Spiget API to download Spigot plugins and wget to download Bukkit or other directly downloadable plugins. Additionally, it can also use other methods that you specify.
