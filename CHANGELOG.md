# MiLibrisReaderSDK Android changelog

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