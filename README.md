# MiLibrisReaderSDK

MiLibrisReaderSDK is the new miLibris reading SDK (previously called MLPDFReaderSDK). It includes the MLFoundation library which allows unpacking miLibris contents.

- [Prerequisites](#prerequisites)
    - [Setup](#setup)
- [Implementation](#implementation)
    * [Usual workflow](#usual-workflow)
    - [Download a complete archive](#download-a-complete-archive)
    - [Unpack a complete archive with MLFoundation](#unpack-a-complete-archive-with-mlfoundation)
    - [Read unpacked contents](#read-unpacked-contents)
- [Customization](#customization)
    - [Optional Features](#optional-features)
        - [Print](#print)
        - [Search publication](#search-publication)
        - [Interstitial advert](#interstitial-advert)
    - [Configure the reader tutorial](#configure-the-reader-tutorial)
    - [Event tracking](#event-tracking)
    - [Resume reading at the last read page](#resume-reading-at-the-last-read-page)
    - [Provide article sharing functionality](#provide-article-sharing-functionality)
    - [Customize logo](#customize-logo)
        - [`DisplayMode` variants in article reader](#displaymode-variants-in-article-reader)
        - [Override capsule](#override-capsule)
            - [Remove the capsule](#remove-the-capsule)
    - [Apply your branding to the reader UI](#apply-your-branding-to-the-reader-ui)
- [Sample project](#sample-project)

## Prerequisites

MiLibrisReaderSDK developed in Kotlin requires Android 5 or higher, and uses glide to load images as external library and other androidX libraries like ViewModel and appCompact.  
Every app using the SDK must be configured with a licence key provided by miLibris. A licence key cannot be used in more than one application.

### Setup

The library is available on our maven and can be added to your Android project as follow:

```groovy
repositories {
    google()
    mavenCentral()
    maven { url 'https://maven-android-sdk.milibris.net/' }
}

dependencies {
    def miLibrisReader = "1.10.2"
    api("com.milibris:one-reader:$miLibrisReader") {   //If you ever have conflict with the version used in our library add this line
        exclude group: "androidx.lifecycle"
    }
    api "com.milibris:milibris-reader:$miLibrisReader"
}
```  

In order for the SDK to work properly you need to add the licence key provided in your manifest as below

```xml

<application....>
    <meta-data android:name="com.milibris.pdfreader.licencekey" android:value="YOUR_LICENCE_KEY" />
</application>  
```  

## Implementation

### Usual workflow

In order to read a content, your application will likely implement the following steps:

1. Download a complete archive (with the *.complete extension) from the miLibris  
   platform.
2. Unpack the archive using MLFoundation
3. Launch Reader to read the unpacked contents

### Download a complete archive

Refer to the miLibris API documentation to obtain a ticket for the issue you want to download, in the `x-ml-pdf` format.

You can then download the complete archive with the following URL: `https://content.milibris.com/access/%@/download.complete` (replace `%@` with the mid of your ticket).

The JWT token returned by the API can be ignored.

### Unpack a complete archive with MLFoundation

A complete archive can be easily unpacked with the MLFoundation library utilities (see  
example below, extracting a sample.complete file in Android assets).

```kotlin
private fun unpackArchive() {
    val foundationContext: com.milibris.foundation.FoundationContext =
        com.milibris.foundation.Foundation.createContext(applicationContext)
    try {
        val archive: com.milibris.foundation.CompleteArchive =
            com.milibris.foundation.CompleteArchive(
                foundationContext,
                assets.openFd("$pdfName.complete").createInputStream()
            )
        archive.unpackTo(File(getExternalFilesDir(null), pdfName))
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}  
```  

### Read unpacked contents

Once unpacked, you can open the content by starting OneReaderActivity:

```kotlin
// Initialize the reader to open the contents val productRepo = XmlPdfReaderDataSource(readerSettings) productRepo.init(applicationContext, contentPath)  
startActivity(
    OneReaderActivity.newIntent(
        context = this,
        readerSettings = ReaderSettings(),
        productRepository = productRepo,
        readerListener = ORListener(dataSource = productRepo, "issueMid", this),
        searchProvider = CustomSearchProvider(), // @see Search publication optional feature
        sharedElementImageUrl = coverImageURL,
        sharedElementRatio = coverRatio
    )
)
```  

## Customization

### Optional Features

We are providing a ReaderSettings class where you can customize the reader as you please to enable or disable some features.

```kotlin
 val readerSettings = ReaderSettings().apply {
    isFaceCropEnabled = true
    debugBoxes = true
    targetPageNumber = targetPage
    textToSpeechEnabled = true
    shareEnabled = true
    enableSummaryImages = true
    logo = R.drawable.milibris
    isSummaryEnabled = true
    isPrintEnabled = true
}
```

#### Print

To allow users to print currently displayed pages we enable the `ReaderSettings` `isPrintEnabled` option.
This feature is disabled by default.

#### Search publication

To enable searching in a publication, we are providing a provider:
```kotlin
OneReaderActivity.newIntent(
    searchProvider = CustomSearchProvider(),
)
```

This object implements the `SearchProvider` interface:

```kotlin
interface SearchProvider {
    fun search(searchText: String, completion: (Result<SearchResponse>) -> Unit)
}
```

@see [CustomSearchProvider.kt](app/src/main/java/com/milibris/reader/sdk/sample/CustomSearchProvider.kt)

The search result can be:
- a list of matching results `SearchResponseItem`
- an empty list to present a "no result" screen
- a failure to present a "technical error" screen

Each `SearchResponseItem` must describe a `SearchResponseItemArticle` and a list of highlights `String`.

For now:
- only the first `highlight` is presented.
- the `SearchResponse` `suggestions` are not used.

By default, without any `SearchProvider` provided, this feature is disabled.

#### Interstitial advert

The reader can present a page as an interstitial advert when an issue is opened.
To enable this feature, the reader must be provided a `PageAdRepository` implementation.
The miLibris reader includes a default one that allows the interstitial to be shown the first time an issue is opened:

```kotlin
OneReaderActivity.newIntent(
    productRepository = productRepo,
    pageAdRepository = MiLibrisPageAdRepository(productRepository),
)
```

### Configure the reader tutorial

The reader is configured to display a tutorial the first time that it is opened on a new device. You can disable it you want:

```kotlin
 val readerSettings = ReaderSettings().apply {
    showReaderTutorials = false
} 
```

Or this way if you don't want the tutorial to be shown on previews for example

```kotlin
 class ORListener(
    private val dataSource: XmlPdfReaderDataSource
) : ReaderListener {

    override fun canOpenTutorials(): Boolean {
        return dataSource.materialParser.isPreview.not()
    }
}
```

### Event tracking

If you want to track user events on reader you need to implement create your own class that implements The ReaderListener :

```kotlin
  class ORListener : ReaderListener {
    override fun onCloseButtonClickListener() {}
    override fun onIssueRead() {}
    override fun onIssuePageRead(pageNumber: Int, isCalledFromArticles: Boolean) {}
    override fun onSummaryOpened(isOpenedFromArticles: Boolean) {}
    // ....  
}
```  

### Resume reading at the last read page

When users close the reader and later open the same issue again, they might expect the reader to open at the last page that they consulted. You can implement this feature in two steps:

- Implement the ReaderListener to save the last page consulted by a user
- Open the reader with the last consulted page

```kotlin
private var targetPage: Int = 1
private fun openReader() {
    startActivity(
        OneReaderActivity.newIntent(
            this,
            ReaderSettings().apply {
                targetPageNumber = targetPage
            },
            productRepo,
            ORListener()
        )
    )
}

class ORListener : ReaderListener {
    override fun onIssuePageRead(pageNumber: Int, isCalledFromArticles: Boolean) {
        targetPage = pageNumber
    }
}
```

### Provide article sharing functionality

You can provide a sharing provider to the reader in order to add a "Share" button on articles. Your will be responsible for providing the URL to share for an article.

- Activate sharing option in ReaderSettting by setting shareURl
- Implement your own sharing login in ReaderListener
- Open the reader with the last consulted page

```kotlin
val readerSetting = ReaderSettings().apply {
    shareEnabled = true
}

class ORListener(
    private val dataSource: XmlPdfReaderDataSource,
    private val context: Context
) : ReaderListener {
    override fun onShareClicked(article: IArticle) {
        val milibrisArticle = dataSource.getMilibrisArticle(article)
        val articleUrl = "${dataSource.readerSettings.shareUrl}/share/article/$issueMid/${milibrisArticle?.mid}"
        val intentBuilder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText(articleUrl)
        intentBuilder.startChooser()
    }
}
```

### Customize logo

#### `DisplayMode` variants in article reader

While reading articles, user can change the `DisplayMode` to `AUTO`, `LIGHT`, or `DARK`.

To match this configuration, we can set a logo for each of these options.
```kotlin
val readerSetting = ReaderSettings().apply {
    logo = R.drawable.milibris                 // AUTO
    logoLight = R.drawable.milibris_light      // LIGHT
    logoDark = R.drawable.milibris_dark        // DARK
}
```
_If `logoLight` and `logoDark` are not provided, `logo` will be used._

The `AUTO` mode relies on Android built-in configuration based drawable:
```
├── res
│   ├── drawable
│   │   ├── milibris.xml          // Used in AUTO when OS is in light mode
│   │   ├── milibris_light.png
│   │   ├── milibris_dark.png
│   ├── drawable-night
│   │   ├── milibris.xml          // Used in AUTO when OS is in dark mode

```

#### Override capsule

Logo appearance is defined by the `ORLogo` style:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="ORLogo">
        <item name="android:layout_width">@dimen/or_logo_capsule_width</item>       <!-- 100dp -->
        <item name="android:layout_height">@dimen/or_logo_capsule_height</item>     <!--  36dp -->
        <item name="android:padding">@dimen/half_margin</item>                      <!--   8dp -->
        <item name="android:background">@drawable/or_logo_capsule_background</item> <!-- background drawable -->
    </style>
</resources>
```

where `android:background` is overriden when `DisplayMode` changes to use:

```kotlin
@drawable/or_logo_capsule_stroke_color                    // AUTO
@drawable/or_logo_capsule_stroke_color_light.xml          // LIGHT
@drawable/or_logo_capsule_stroke_color_dark.xml           // DARK
```
_these are simple corner rounded bordered shapes_

##### Remove the capsule

To completely remove the capsule, you can override this style to remove the padding:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="ORLogo">
        <item name="android:layout_width">@dimen/or_logo_capsule_width</item>
        <item name="android:layout_height">@dimen/or_logo_capsule_height</item>
        <item name="android:padding">0dp</item> <!-- set the padding to zero -->
        <item name="android:background">@drawable/or_logo_capsule_background</item>
    </style>
</resources>
```
and override the three drawable resources used as background with empty drawables:
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector />
```

### Apply your branding to the reader UI

Many components of the reader UI can be customized to match your brand, And for you to do that you just need to override the definition os some color and drawable ressources. The complete reference can be found in [docs/config.md](./docs/config.md#readerconfig).

## Sample project

A sample project is provided to help you implement the reader integration. It contains an example to unpack a complete archive and to open if with PdfReader class.

If your miLibris content has articles, you can implement your own sharing solution by adding your own code in onShareClicked() just like shown in this sample in the class ORListener
