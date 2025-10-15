# MiLibrisReaderSDK Android changelog

# 1.17.2

## Improvements
- Fix on interstitial translations for all languages

# 1.17.1

## Improvements
- Fix a case where article reader settings event wasn't send to `ReaderListener`

# 1.17.0

## Features
- Add a way to inject a `Logger` to intercept OneReader logs
- Rework obfuscation to prevent conflicts with other obfuscated library

# 1.16.1

## Improvements
- Fix on support for [16 KB page sizes](https://developer.android.com/guide/practices/page-sizes)

# 1.16.0

## Features
- Support for [16 KB page sizes](https://developer.android.com/guide/practices/page-sizes)

# 1.15.0

## Features
- Add an option to override default search in publication endpoint

# 1.14.0

## Features
- `ReaderListener` receives notifications when article reader top app bar title or logo is clicked

# 1.13.4

## Improvements
- Fix a potential crash from the interstitial advert shown when opening a release

# 1.13.3

## Improvements
- Fix boxes rendering: article boxes will no longer appear when UI is toggled to be shown

# 1.13.2

## Improvements
- Fix reader pagination when opened in offline mode

# 1.13.1

## Improvements
- Fix images format support for miLibris articles

# 1.13.0

## Features
- `ReaderListener` receives notifications when PDF reader and article reader goes into background and comes back to foreground

## Improvements
- Fix on `ReaderListener.onArticlesOpen` not receiving the currently visible article when article reader is opened standalone

# 1.12.0

## Features
- Upgrade to Android 15 and display content edge-to-edge

# 1.11.0

## Features
- Add `ReaderListener.onArticlesClosed` to notify when article is closed (while swiping or closing the reader).

# 1.10.5

## Improvements
- Fix a rare crash on cache management
- Fix rares crashes on screen updates

# 1.10.4

## Improvements
- Fix on text to speech so it is disabled when `textToSpeechEnabled` is set to `false`

# 1.10.3

## Improvements
- Fix on the page as interstitial issue state in the miLibris implementation

# 1.10.2

## Improvements
- Fix an ANR on article reader when dropcap text is empty

# 1.10.1

## Improvements
- Fix a bug preventing cover zoom-in animation transition – _introduced in `1.10.0`_

# 1.10.0

## Features
- Add an alternative tutorial screen for when long press on article is enabled
- Add an option to let users select article text – _disabled by default_
- Add visual feedback on interactions with article reader bottom bar

> [!NOTE]  
> The `ReaderListener.shouldOpenArticle` function prototype had to be changed to comply with an internal change.
> Diff to update to this change:
```diff
- override fun shouldOpenArticle(oneReaderActivity: OneReaderActivity, article: IArticle): Boolean
+ override fun shouldOpenArticle(oneReaderActivity: FragmentActivity, article: IArticle): Boolean
```

# 1.9.1

## Improvements
- Prevent interaction with PDF reader when page as an interstitial advert is shown
- Fix pagination on slideshow enrichments
- Fix pagination on advert article in article reader

# 1.9.0

## Features
- Show a given page as an interstitial advert when opening an edition
- Improve multimedia boxes visibility

## Improvements
- Fix pagination sensitivity to improve panning inside a zoomed page
- Fix potential partial blurred PDF renders

# 1.8.2

## Improvements
- Allow article rubric to be multi-line to prevent ellipsis

# 1.8.1

## Improvements
- Match double tap zoom with iOS implementation
- Fix PDF rendering performances that could be progressively slowed down

# 1.8.0

## Features
- Add text to speech in background
- Add support for releases built on multiple PDFs containing multiple pages

## Improvements
- Fix HTML decoding on titles, abstract and author
- Fix a crash that could occur on invalid summary definitions

# 1.7.0

## Features
- Add an intermediate configuration for fonts
- Hide article counter capsule and print button when zooming
- Add an option to force articles layout
- Add an option to trigger an event when a page stayed open for `x` seconds

# 1.6.3

## Improvements
- Fixes a crash that could occur when application is restored
- Fixes crashes that could occur when article/pdf reader was closed

# 1.6.2

## Improvements
- Fixes compatibility with R8 full mode
- Fixes a crash that could occur on API 21 when search feature was implemented
- Fixes thrown error when miLibris reader manifest cannot be read
- Strips html tags from photos credits

# 1.6.1

## Improvements
- Fixes summary parsing in some release cases
- Fixes article reader not opening when no `ReaderListener` is provided

# 1.6.0

## Features
- Add support for advertisement articles

# 1.5.2

## Improvements
- Fixes article reader image caption format

# 1.5.1

## Improvements
- Fixes crashes that could occur due to session
- Fixes article reader caption duplication
- Adds missing credits in the article reader photo caption in automatic template resolution

# 1.5.0

## Features
- Improved error handling

## Improvements
- Fixes crashes due to session
- Fixes crashes due to cache
- Fixes crashes due to PDF rendering
- Fixes on configuration changes

# 1.4.0

## Features
- Add an option to allow users to print currently displayed pages
- Add an option to search in publication

# 1.3.1

## Improvements
- Fix crash on PDF page rendering
- Fix crash on article reader navigation

# 1.3.0

## Features
- Performance improvements on PDF rendering
- Memory usage improvements on PDF rendering

# 1.2.1

## Improvements
- Add missing credits in the article reader photo caption
- Fix header date format
- Fix typing error in french tutorial

# 1.2.0

## Features
- Add an option to hide the summary

# 1.1.1

## Improvements
- Article drop cap drawing

# 1.1.0

## Features
- Add a light/dark options for logo

# 1.0.4

07/08/2023

## Improvements

- Fix page rendering cache cleanup

# 1.0.3

13/07/2023

## Improvements

- Page rendering performances improvements

# 1.0.2

## Improvements

- Native article dark/light mode improvements

# 1.0.1

## FIXED
- Fix loop on TTS button  (#241)

# 1.0.0

## Features
- First version with native articles (#237)
- Articles with different template (#237)
- New block containing next article (#237)

# 0.24.0

24/04/2023

## FIXED

- Fix Memory Leak  (#236)

# 0.23.2

04/04/2023

## FIXED

- Dark and light Mode on WebView  (#235)

# 0.23.1

22/03/2023

## Improvements

- Add style for Logo that can be overrided easily to change width height background (#234)
- fix padding on logo capsule (#234)

# 0.23.0

22/03/2023

## Improvements

- Migrate package name from co.cafeyn to com.milibris  (#231)

# 0.22.0

22/03/2023

## Improvements

- Refactor PDF reader to improve memory management and fix all OutOfMemoryException  (#230)

# 0.21.1

20/02/2023

## Features

- Update status bar color according to toolbar color  (#222)
- Add tutorials (#220)
- New config for TTS preferred language (#223)

## Improvements

- Design improvement  (#224)

### v0.20.0

05/10/2022

- First Beta version of the new Milibris reader 