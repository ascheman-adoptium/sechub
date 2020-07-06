// SPDX-License-Identifier: MIT
package cli

import (
	"fmt"
	"os"
	"path/filepath"
	"testing"

	. "daimler.com/sechub/testutil"
	. "daimler.com/sechub/util"
)

func TestReportFilePathCorrectCreated(t *testing.T) {
	/* prepare */
	report := Report{serverResult: []byte("content"), outputFolder: "path1", outputFileName: "fileName1"}

	/* execute */
	result := report.createFilePath(false)

	/* test */
	expected := filepath.Join("path1", "fileName1")
	if result != expected {
		t.Fatalf("Strings differ:\nExpected:%s\nGot     :%s", expected, result)
	}
}

func TestReportSaveWritesAFile(t *testing.T) {
	/* prepare */
	tempDir := InitializeTestTempDir(t)
	defer os.RemoveAll(tempDir)

	report := Report{serverResult: []byte("content"), outputFolder: tempDir, outputFileName: "a.out"}
	fmt.Printf("Report: %q\n", report)

	var config Config
	context := NewContext(&config)

	/* execute */
	report.save(context)

	/* test */
	expected := filepath.Join(tempDir, "a.out")
	AssertFileExists(expected, t)
}
