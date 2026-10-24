# Dock icon vectors

The dock uses Android `VectorDrawable` resources for YouTube, Prime Video, Netflix, and Willow, referenced by `HomeActivity`. The older PNG assets are not used by the dock.

The source marks were redrawn from vector artwork so the existing 512 × 308 composition stays crisp at any screen scale:

- YouTube: [YouTube Logo 2017 (Wikimedia Commons)](https://commons.wikimedia.org/wiki/File:YouTube_Logo_2017.svg). The play mark remains red and white; the wordmark is red on the black tile.
- Prime Video: [Prime Video logo (2024), sourced from Amazon.com (Wikimedia Commons)](https://commons.wikimedia.org/wiki/File:Prime_Video_logo_(2024).svg). The mark is blue on the black tile.
- Netflix: [Netflix 2015 logo (Wikimedia Commons)](https://commons.wikimedia.org/wiki/File:Netflix_2015_logo.svg). The wordmark is red on the black tile.
- Willow: the white Willow / Cricbuzz lockup comes from the installed Willow app's vector banner artwork; its green background paths were omitted for the black tile.

The `*-source.svg` files retain the original source geometry for YouTube, Prime Video, and Netflix. The app-ready vector resources are in `app/src/main/res/drawable-nodpi/`.

Morning art uses white rounded icon faces with contrast-safe brand colors. Editable SVG previews are saved as morning-*.svg; the Android morning VectorDrawable resources are in `app/src/main/res/drawable-nodpi/`. Spotify’s morning mark is drawn as vector geometry with a system-rendered wordmark so it stays sharp at dock size.
