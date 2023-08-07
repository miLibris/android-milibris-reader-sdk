# MiLibrisReaderSDK

MiLibrisReaderSDK is the new miLibris reading SDK (previously called MLPDFReaderSDK). It includes the MLFoundation library which allows unpacking miLibris contents.

* [Prerequisites](#prerequisites)
    * [Setup](#setup)
    * [Implementation](#implementation)
    * [Unpack archive](#unpack-a-complete-archive-with-mlfoundation)
    * [Open reader](#read-unpacked-contents)
    * [Customization](#customization)
    * [Optional features](#optional-features)
    * [Configure the reader tutorial](#configure-the-reader-tutorial)
    * [Event tracking](#event-tracking)
    * [Resume reading](#resume-reading-at-the-last-read-page)
    * [Sharing](#provide-article-sharing-functionality)
    * [Branding](#apply-your-branding-to-the-reader-ui)

### Prerequisites

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
    api("com.milibris:one-reader:1.0.4") {   //If you ever have conflict with the version used in our libary add this line  
        exclude group: "androidx.lifecycle"
    }
    api("com.milibris:milibris-reader:1.0.4")
}  
```  

In order for the SDK to work properly you need to add the licence key provided in your manifest as below

```xml

<application....>
    <meta-data android:name="com.milibris.pdfreader.licencekey" android:value="YOUR_LICENCE_KEY" />
</application>  
```  

# Implementation

In order to read a content, your application will likely implement the following steps:

1. Download a complete archive (with the *.complete extension) from the miLibris  
   platform.
2. Unpack the archive using MLFoundation
3. Launch Reader to read the unpacked contents

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
        this,
        ReaderSettings(),
        productRepo,
        ORListener(dataSource = productRepo, "issueMid", this),
        coverImageURL,
        coverRatio
    )
)  
```  

# Customization

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
}  
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

### Apply your branding to the reader UI

Many components of the reader UI can be customized to match your brand, And for you to do that you just need to override the definition os some color and drawable ressources. The complete reference can be found in [docs/config.md](./docs/config.md#readerconfig).

### Sample project

A sample project is provided to help you implement the reader integration. It contains an example to unpack a complete archive and to open if with PdfReader class.

If your miLibris content has articles, you can implement your own sharing solution by adding your own code in onShareClicked() just like shown in this sample in the class ORListener
