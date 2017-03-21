package no.mofifo.imber.retrofit;

import okhttp3.Headers;

/**
 * Created by andre on 03.01.17.
 */

public class PaginationUtils {

    /**
     * Parses a set of pagination links from the "Link" HTTP header, and returns the link to the next page.
     *
     * @param headers The HTTP header.
     * @return The link to the next page, or an empty string if there is no such link.
     */
    public static String getNextPageUrl(Headers headers) {

        if (headers.get("Link") != null) {
            String[] links = headers.get("Link").split(",");

            for (int i = 0; i < links.length; i++) {
                String line = links[i];
                // If link contains rel=next
                if (line.contains("rel=\"next\"")) {

                    // Get next link url
                    String link = line.substring(line.indexOf("<") + 1);
                    link = link.substring(0, link.indexOf(">"));

                    // There is only one 'rel=next' link in each response
                    return link;
                }
            }
        }
        // No next link
        return  "";
    }

}
