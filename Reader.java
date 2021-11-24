import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * A class for reading the MNIST data set from the <b>decompressed</b>
 * (unzipped) files that are published at
 * <a href="http://yann.lecun.com/exdb/mnist/">
 * http://yann.lecun.com/exdb/mnist/</a>.
 *
 * Original code by @RayDeeA on StackOverflow here:
 * https://stackoverflow.com/a/20383900/16755079
 *
 * I have made edits and comments to that code
 */
public class Reader {
    public static final int TYPE_FILE_IDX_IMAGE = 0;

    private int fileType;
    private FileInputStream file;
    private int magicNumber;
    private int[] dimensions;

    private int currentIndex; //counting through dimensions[0]

    public Reader(String path, int fileType) {
        this.fileType = fileType;

        if (fileType == TYPE_FILE_IDX_IMAGE) {
            setupIDXImage(path);
        } else {
            // throw FileTypeNotFoundException;
        }
    }

    public int getImageSize() {
        int size = 1;
        for (int i = 1; i < dimensions.length; i++) {
            size *= dimensions[i];
        }
        return size;
    }

    private void setupIDXImage(String path) {
        try {
            file = new FileInputStream(path);
            /**
             * The basic format for idx files is:
             * magic number
             * size in dimension 0
             * size in dimension 1
             * size in dimension 2
             * .....
             * size in dimension N
             * data
             */

            /**
             * The magic number is an integer (MSB first). The first 2 bytes are always 0.
             *
             * The third byte codes the type of the data:
             * 0x08: unsigned byte
             * 0x09: signed byte
             * 0x0B: short (2 bytes)
             * 0x0C: int (4 bytes)
             * 0x0D: float (4 bytes)
             * 0x0E: double (8 bytes)
             */
            this.magicNumber = (file.read() << 24) | (file.read() << 16) | (file.read() << 8) | (file.read());

            /**
             * The 4-th byte codes the number of dimensions of the vector/matrix: 1 for vectors, 2 for matrices....
             */
            int dimension_count = 0xF & magicNumber;
            this.dimensions = new int[dimension_count];
            /**
            * The sizes in each dimension are 4-byte integers (MSB first, high endian, like in most non-Intel processors).
            *
            * The data is stored like in a C array, i.e. the index in the last dimension changes the fastest.
            */
            for (int i = 0; i < dimensions.length; i++) {
                dimensions[i] = (file.read() << 24) | (file.read() << 16) | (file.read() << 8) | (file.read());
            }

            this.currentIndex = 0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return the first len sets of data
     * @params len : the number of sets, if len would cause currentIndex to surpass dimensions[0], only return the the rest of the file
     *
     * Commented out code here can be used for creating PNG files of the image
     */

    public int[][] read(int len) {
        // we can basically read the image as a long line of pixels.
        // pixels wrap around to the next line when you reach the end.
        int numberOfPixels = 1;
        for (int i = 1; i < dimensions.length; i++) {
            numberOfPixels *= dimensions[i];
        }
        System.out.println("Num pxl = " + numberOfPixels);

        // int[] imgPixels = new int[numberOfPixels];
        // BufferedImage image = new BufferedImage(dimensions[2], dimensions[1], BufferedImage.TYPE_INT_ARGB);

        //indexing stuff
        int endIndex;
        if (currentIndex + len > dimensions[0]) {
            endIndex = dimensions[0];
        } else {
            endIndex = currentIndex + len;
        }

        int[][] batch = new int[endIndex-currentIndex][numberOfPixels];


        for (int i = 0; i < batch.length; i++) {
            currentIndex++;

            //log every 100 images
            if(currentIndex % 100 == 0) {System.out.println("Number of images extracted: " + currentIndex);}

            for (int p = 0; p < numberOfPixels; p++) {
                try {
                    int gray = file.read();
                    batch[i][p] = gray;
                    // gray = 255 - gray;
                    // imgPixels[p] = 0xFF000000 | (gray<<16) | (gray<<8) | gray;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // image.setRGB(0, 0, dimensions[2], dimensions[1], imgPixels, 0, dimensions[2]);
            // File outputfile = new File("./Number_Images/" + "_0" + currentIndex + ".png");
            // try {
            //     ImageIO.write(image, "png", outputfile);
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
        }

        return batch;
    }

    /**
     * read the next line of file
     */
    public int[][] read() {
        return read(1);
    }

    /**
     * This is the original code written by @RayDeeA (with added comments).
     *
     * Commented out to avoid accidental overwritting
    public static void read() {

         * a FileInputStream is basically a stream of bytes.
         * We will referrence the file to get the stream from
         * an actual file in the file system. This file is
         * selected with the `inputImagePath` and  `inputLabelPath`.

        FileInputStream inImage = null;
        FileInputStream inLabel = null;

        // change the `.` according to where you put the files
        String inputImagePath = "./train-images-idx3-ubyte";
        String inputLabelPath = "./train-labels-idx1-ubyte";
        String outputPath = "./";

        //remember that MNIST is a bunch of images for the integers 0-9
        //this will keep track of the frequency of each integer
        int[] hashMap = new int[10];

        try {

             * FileInputStream.read() throws an IOException,
             * so this all has to be in a try-catch. Also,
             * a few other methods throw various exceptions (see
             * the catches below)

            inImage = new FileInputStream(inputImagePath);
            inLabel = new FileInputStream(inputLabelPath);


             * The basic format for idx files is:
             * magic number
             * size in dimension 0
             * size in dimension 1
             * size in dimension 2
             * .....
             * size in dimension N
             * data



             * The magic number is an integer (MSB first). The first 2 bytes are always 0.
             *
             * The third byte codes the type of the data:
             * 0x08: unsigned byte
             * 0x09: signed byte
             * 0x0B: short (2 bytes)
             * 0x0C: int (4 bytes)
             * 0x0D: float (4 bytes)
             * 0x0E: double (8 bytes)
             *
             * The 4-th byte codes the number of dimensions of the vector/matrix: 1 for vectors, 2 for matrices....

            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

             * The sizes in each dimension are 4-byte integers (MSB first, high endian, like in most non-Intel processors).
             *
             * The data is stored like in a C array, i.e. the index in the last dimension changes the fastest.

            int numberOfRows  = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

            //BufferedImage witih size numberOfColumns x numberOfRows using ARGB color space
            BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
            // we can basically read the image as a long line of pixels.
            // pixels wrap around to the next line when you reach the end.
            int numberOfPixels = numberOfRows * numberOfColumns;
            int[] imgPixels = new int[numberOfPixels];

            for (int i = 0; i < numberOfImages; i++) {

                //log every 100 images
                if(i % 100 == 0) {System.out.println("Number of images extracted: " + i);}

                for (int p = 0; p < numberOfPixels; p++) {
                    //MNIST uses 0 as white -> 255 as black
                    //ARGB uses 255 as white -> 0 as black
                    int gray = 255 - inImage.read(); //flip the byte
                    //fully opaque with repeating `gray` bytes
                    //basically, gray scale the pixel
                    imgPixels[p] = 0xFF000000 | (gray<<16) | (gray<<8) | gray;
                }

                //fill the entire `image` with the values dictated by `imgPixels`
                image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);

                int label = inLabel.read();

                hashMap[label]++;
                File outputfile = new File(outputPath + label + "_0" + hashMap[label] + ".png");

                //This would write the image to your root directory
                //Remember that this happens a couple tens of thousands of times
                //It is generally not a good idea
                // ImageIO.write(image, "png", outputfile);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // Clean up the files
            if (inImage != null) {
                try {
                    inImage.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inLabel != null) {
                try {
                    inLabel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    */
}
