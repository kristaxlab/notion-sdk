# GitHub Pages Template

This directory contains the template files for the GitHub Pages deployment.

## Files

- **index.html** - Universal test reports viewer that dynamically loads reports from `reports.json`

## How It Works

1. The GitHub Actions workflow generates test reports in the `jacoco/` directory
2. Each report folder is named: `{branch}-{date}-{time}-{commit}`
3. A `.metadata` file stores the author and timestamp for each report
4. The workflow generates `reports.json` containing metadata for all reports
5. `index.html` fetches and displays all reports dynamically

## Features

- **Dynamic Loading**: No need to rebuild HTML when new reports are added
- **Filtering**: Filter reports by branch, date, or commit
- **Time Display**: Shows relative time (e.g., "5min ago", "2d ago")
- **Author Info**: Displays who created each report
- **Responsive**: Works on all screen sizes

## Customization

To customize the appearance, edit the `<style>` section in `index.html`.

To add new features, modify the JavaScript section at the bottom of `index.html`.
