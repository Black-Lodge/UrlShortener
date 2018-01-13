package urlshortener.blacklodge.services;

/**
 * Interface for image generator services
 */
public interface MemeImageGeneratorService {
    /**
     * Generates a meme image based on the two words that are given
     * @param noun First word of the meme
     * @param adj Second word of the meme
     * @return URL of the generated image
     */
    String generateImage(String noun, String adj);
}
