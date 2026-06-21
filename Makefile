# ============================================================
#  Makefile — Library Management System
#  Usage:
#    make        → compile
#    make run    → compile + run
#    make clean  → remove compiled classes
# ============================================================

SRC_DIR   = src
OUT_DIR   = out
MAIN      = library.LibraryApp

SOURCES   := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all run clean

all: $(OUT_DIR)
	javac -d $(OUT_DIR) -sourcepath $(SRC_DIR) $(SOURCES)
	@echo ""
	@echo "  ✓  Compiled successfully. Run with: make run"

$(OUT_DIR):
	mkdir -p $(OUT_DIR)

run: all
	@echo ""
	java -cp $(OUT_DIR) $(MAIN)

clean:
	rm -rf $(OUT_DIR)
	@echo "  ✓  Cleaned compiled output."
