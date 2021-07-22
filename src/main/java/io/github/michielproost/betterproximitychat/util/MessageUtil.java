package io.github.michielproost.betterproximitychat.util;

import java.util.Random;

/**
 * Class that provides functions related to messages.
 * @author Michiel Proost
 */
public class MessageUtil {

    /**
     * Add noise to a given message.
     * @param message The message.
     * @param errorChance The chance of error for each individual character.
     * @return The noisy message.
     */
    public static String addNoise( String message, double errorChance )
    {
        // Random generator.
        Random random = new Random();
        // Build the new message.
        StringBuilder noisyMessage = new StringBuilder();
        // For each character.
        for (int i = 0; i < message.length(); i++)
        {
            if ( Math.random() <= errorChance ){
                // Error: Add random ASCII character.
                noisyMessage.append( getRandomASCIICharacter() );
            } else {
                // No error: Add original character.
                noisyMessage.append( getCharFromString( message, i ) );
            }
        }
        // Return the new message.
        return noisyMessage.toString();
    }

    /**
     * Get character from a string given an index.
     * @param str The string.
     * @param index The index.
     * @return The character at the given index.
     */
    public static char getCharFromString( String str, int index )
    {
        return str.charAt( index );
    }

    /**
     * Get a random ASCII character.
     * @return A random ASCII Character.
     */
    public static char getRandomASCIICharacter()
    {
        int ASCII_MAX = 127;
        int ASCII_MIN = 32;
        Random random = new Random();
        int index = random.nextInt( ASCII_MAX - ASCII_MIN ) + ASCII_MIN;
        return (char) index;
    }

}