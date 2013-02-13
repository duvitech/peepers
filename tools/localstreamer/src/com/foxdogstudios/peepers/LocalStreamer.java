package com.foxdogstudios.peepers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocalStreamer
{

    public static void main(final String[] args) throws InterruptedException
    {
        if (args.length != 5)
        {
            System.err.println("Usage: localstreamer host_name port jpeg width height");
            return;
        } // if

        final String hostName = args[0];
        final int port = Integer.parseInt(args[1]);
        final String fileName = args[2];
        final int jpegWidth = Integer.parseInt(args[3]);
        final int jpegHeight = Integer.parseInt(args[4]);

        final RandomAccessFile jpegFile;
        try
        {
            jpegFile = new RandomAccessFile(fileName, "r");
        } // try
        catch (FileNotFoundException e)
        {
            System.err.println("Could not find file '" + fileName + "'");
            return;
        } // catch

        final int jpegLength;
        try
        {
            jpegLength = (int) jpegFile.length();
        } // try
        catch (IOException e)
        {
            System.err.println("Could not get length of file '" + fileName + "'");
            return;
        } // catch

        final byte[] jpegBuffer = new byte[jpegLength];
        try
        {
            jpegFile.read(jpegBuffer);
        } // try
        catch (IOException e)
        {
            System.err.println("Could not read file '" + fileName + "'");
            return;
        } // catch

        final MJpegRtpStreamer mJpegRtpStreamer;
        try
        {
            mJpegRtpStreamer = new MJpegRtpStreamer(hostName, port);
        } // try
        catch (IOException e)
        {
            System.err.println("Could not create MJpegRtpStreamer()");
            return;
        } // catch

        System.out.println("Beginning MJPEG stream, Ctrl-C to stop");

        while (true)
        {
            final long timeStamp = System.currentTimeMillis();
            try
            {
                mJpegRtpStreamer.sendJpeg(jpegBuffer, jpegLength, jpegWidth, jpegHeight,
                        timeStamp);
            } // try
            catch (IOException e)
            {
                System.err.println("Could not send jpeg");
            } // catch
            Thread.sleep(1000);
        } // while

    } // main(String[])
} // class LocalStreamer
