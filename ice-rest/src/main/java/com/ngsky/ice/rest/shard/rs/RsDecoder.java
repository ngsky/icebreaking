package com.ngsky.ice.rest.shard.rs;

import com.ngsky.ice.rest.exception.ForbiddenException;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/8/5 19:07
 */
public class RsDecoder {
    private int dataShards;
    private int parityShards;
    private int TOTAL_SHARDS;
    public static final int BYTES_IN_INT = 4;

    public RsDecoder(int dataShards, int parityShards) {
        this.dataShards = dataShards;
        this.parityShards = parityShards;
        this.TOTAL_SHARDS = dataShards + parityShards;
    }

    public File decode(String filePath, String[] objKeys, String objDir) throws ForbiddenException, IOException {
        final File originalFile = new File(filePath);

        final byte[][] shards = new byte[TOTAL_SHARDS][];
        final boolean[] shardPresent = new boolean[TOTAL_SHARDS];
        int shardSize = 0;
        int shardCount = 0;
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            File shardFile = new File(
                    objDir,
                    objKeys[i]);
            if (shardFile.exists()) {
                shardSize = (int) shardFile.length();
                shards[i] = new byte[shardSize];
                shardPresent[i] = true;
                shardCount += 1;
                InputStream in = new FileInputStream(shardFile);
                in.read(shards[i], 0, shardSize);
                in.close();
                System.out.println("Read " + shardFile);
            }
        }

        // We need at least DATA_SHARDS to be able to reconstruct the file.
        if (shardCount < dataShards) {
            System.out.println("Not enough shards present");
            return null;
        }

        // Make empty buffers for the missing shards.
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            if (!shardPresent[i]) {
                shards[i] = new byte[shardSize];
            }
        }

        // Use Reed-Solomon to fill in the missing shards
        ReedSolomon reedSolomon = ReedSolomon.create(dataShards, parityShards);
        reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);

        // Combine the data shards into one buffer for convenience.
        // (This is not efficient, but it is convenient.)
        byte[] allBytes = new byte[shardSize * dataShards];
        for (int i = 0; i < dataShards; i++) {
            System.arraycopy(shards[i], 0, allBytes, shardSize * i, shardSize);
        }

        // Extract the file length
        int fileSize = ByteBuffer.wrap(allBytes).getInt();

        // Write the decoded file
        File decodedFile = new File(filePath + ".decoded");
        OutputStream out = new FileOutputStream(decodedFile);
        out.write(allBytes, BYTES_IN_INT, fileSize);
        System.out.println("Wrote " + decodedFile);
        return decodedFile;
    }
}
