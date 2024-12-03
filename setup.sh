#!/bin/bash

HIST_PATH="/usr/local/bin/hist"

if [ -f "$HIST_PATH" ]; then
  echo "File $HIST_PATH already exists. Exiting."
  exit 1
fi

echo -e "#!/bin/bash\n\njava -cp \"/home/tayssir/projects/Histograph/target/Histograph-1.0-SNAPSHOT.jar:/home/tayssir/.m2/repository/info/picocli/picocli/4.7.5/picocli-4.7.5.jar\" com.tsts.Main  \"\$@\"" > "$HIST_PATH"

chmod +x "$HIST_PATH"

echo "'hist' script has been created at $HIST_PATH and made executable."
