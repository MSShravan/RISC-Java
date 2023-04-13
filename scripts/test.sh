#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
./gradlew cloverAggregateReports || exit 1
scripts/coverage_summary.sh
ls -l /coverage-out/
ls -l build/reports/
cp -r build/reports/clover/html/* /coverage-out/ || exit 1

