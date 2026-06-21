# ============================================================
#  Makefile — Library Management System (Java)
#  Usage:
#    make          → compile all source files
#    make run      → compile + run
#    make clean    → remove compiled .class files
#    make jar      → package into a runnable JAR
# ============================================================

SRC_DIR   = src
OUT_DIR   = out
JAR_NAME  = LibraryManagementSystem.jar
MAIN      = library.LibraryApp

SOURCES   = $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all run clean jar

all:
	@echo "Compiling..."
	@mkdir -p $(OUT_DIR)
	@javac -sourcepath $(SRC_DIR) -d $(OUT_DIR) $(SOURCES)
	@echo "Build successful. Run with: make run"

run: all
	@java -cp $(OUT_DIR) $(MAIN)

clean:
	@rm -rf $(OUT_DIR)
	@echo "Cleaned."

jar: all
	@echo "Main-Class: $(MAIN)" > manifest.txt
	@jar cfm $(JAR_NAME) manifest.txt -C $(OUT_DIR) .
	@rm manifest.txt
	@echo "Created $(JAR_NAME). Run with: java -jar $(JAR_NAME)"
