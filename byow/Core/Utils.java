package byow.Core;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class Utils {
    Utils() {
    }

    static byte[] readContents(File var0) {
        if (!var0.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        } else {
            try {
                return Files.readAllBytes(var0.toPath());
            } catch (IOException var2) {
                throw new IllegalArgumentException(var2.getMessage());
            }
        }
    }

    static String readContentsAsString(File var0) {
        return new String(readContents(var0), StandardCharsets.UTF_8);
    }

    static void writeContents(File var0, Object... var1) {
        try {
            if (var0.isDirectory()) {
                throw new IllegalArgumentException("cannot overwrite directory");
            } else {
                BufferedOutputStream var2 =
                        new BufferedOutputStream(Files.newOutputStream(var0.toPath()));
                Object[] var3 = var1;
                int var4 = var1.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    Object var6 = var3[var5];
                    if (var6 instanceof byte[]) {
                        var2.write((byte[]) var6);
                    } else {
                        var2.write(((String) var6).getBytes(StandardCharsets.UTF_8));
                    }
                }

                var2.close();
            }
        } catch (ClassCastException | IOException var7) {
            throw new IllegalArgumentException(var7.getMessage());
        }
    }

    static <T extends Serializable> T readObject(File var0, Class<T> var1) {
        try {
            ObjectInputStream var2 = new ObjectInputStream(new FileInputStream(var0));
            Serializable var3 = (Serializable) var1.cast(var2.readObject());
            var2.close();
            return (T) var3;
        } catch (ClassCastException | ClassNotFoundException | IOException var4) {
            throw new IllegalArgumentException(var4.getMessage());
        }
    }

    static void writeObject(File var0, Serializable var1) {
        writeContents(var0, serialize(var1));
    }

    static File join(String var0, String... var1) {
        return Paths.get(var0, var1).toFile();
    }

    static File join(File var0, String... var1) {
        return Paths.get(var0.getPath(), var1).toFile();
    }

    static byte[] serialize(Serializable var0) {
        try {
            ByteArrayOutputStream var1 = new ByteArrayOutputStream();
            ObjectOutputStream var2 = new ObjectOutputStream(var1);
            var2.writeObject(var0);
            var2.close();
            return var1.toByteArray();
        } catch (IOException var3) {
            throw error("Internal error serializing commit.");
        }
    }

    static RuntimeException error(String var0, Object... var1) {
        return new RuntimeException(String.format(var0, var1));
    }
}

