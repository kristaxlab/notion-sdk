# GitHub Pages Template

This directory contains the template files for the GitHub Pages deployment.

## Files

- **index.html** - Universal test reports viewer that dynamically loads reports from `reports.json`

## How It Works

1. The GitHub Actions workflow generates test reports in the `jacoco/` directory
2. Each report folder is named: `{branch}-{date}-{time}-{commit}`
3. Test statistics are extracted from:
   - **JUnit XML files** (`test-results/test/TEST-*.xml`) for test counts (more reliable)
   - **JaCoCo XML report** (`jacocoTestReport.xml`) for coverage percentage
4. A `.metadata` file stores the author, timestamp, and all statistics for each report
5. The workflow generates `reports.json` containing metadata for all reports
6. `index.html` fetches and displays all reports dynamically

## Features

- **Dynamic Loading**: No need to rebuild HTML when new reports are added
- **Filtering**: Filter reports by branch, date, or commit
- **Time Display**: Shows relative time (e.g., "5min ago", "2d ago")
- **Author Info**: Displays who created each report
- **Test Statistics**: Shows pass/fail counts and coverage percentages
- **Responsive**: Works on all screen sizes

## Handling Old Reports

The workflow intelligently handles old report folders that were created before the `.metadata` feature was added:

### Metadata Extraction
For reports without `.metadata` files, the workflow automatically:
- **Extracts test statistics** from existing HTML reports (unit/integration test counts) as fallback
- **Extracts coverage percentage** from JaCoCo XML reports
- **Parses timestamps** from the folder name format (`YYYY-MM-DD-HHhMMmSSs`)
- **Sets author** to "unknown" when not available

**Note**: New reports use JUnit XML files (`TEST-*.xml`) for test statistics, which are more reliable than HTML parsing. Old reports fall back to HTML parsing since the original XML files are not archived.

### Timestamp Fallback
- **Primary**: Reads from `.metadata` file
- **Secondary**: Derives from folder name date/time
- **Fallback**: Uses Unix epoch start time (January 1, 1970 00:00:00 UTC / timestamp `0`) if parsing fails

This ensures old reports are properly displayed with accurate statistics while sorting them appropriately in the timeline.

## Customization

To customize the appearance, edit the `<style>` section in `index.html`.

To add new features, modify the JavaScript section at the bottom of `index.html`.
