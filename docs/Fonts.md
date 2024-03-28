# Fonts

OneReader includes fonts definitions with default fonts:

- `or_primary_black`: Source Serif Pro
- `or_primary_bold`: Source Serif Pro
- `or_secondary_medium`: Roboto
- `or_secondary_regular`: Roboto
- `or_secondary_semibold`: Roboto
- `or_tertiary_bold`: Roboto
- `or_tertiary_regular`: Roboto

## Override

To provide custom fonts, you can override these definitions by override their `font` resource file, as it can be seen in the [../app/src/main/res/font](../app/src/main/res/font) directory.

## Mapping with Article content

- `or_primary_black`: title
- `or_primary_bold`: intertitle, quote, quoteAuthor, next article title
- `or_secondary_medium`: rubrics, theme
- `or_secondary_regular`: author, readingTime, next article [label], next article readingTime
- `or_secondary_semibold`: subtitle, surtitle
- `or_tertiary_bold`: heading, interviewQuestion
- `or_tertiary_regular`: paragraph, interviewAnswer, note, caption
