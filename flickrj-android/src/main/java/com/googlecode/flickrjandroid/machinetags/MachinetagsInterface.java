package com.googlecode.flickrjandroid.machinetags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;

/**
 * A more precise way to tag and to search photos.
 * 
<h3>What are machine tags?</h3>

Machine tags are tags that use a special syntax to define extra information
about a tag.<br>

Machine tags have a namespace, a predicate and a value. The namespace defines a class or a facet that a tag belongs to ('geo', 'flickr', etc.) The predicate is name of the property for a namespace ('latitude', 'user', etc.) The value is, well, the value.<br>

Like tags, there are no rules for machine tags beyond the syntax to specify the parts of a machine tag. For example, you could tag a photo with:<p>

<ul>
    <li>flickr:user=straup</li>
    <li>flora:tree=coniferous</li>
    <li>medium:paint=oil</li>
    <li>geo:quartier="plateau mont royal"</li>
    <li>geo:neighbourhood=geo:quartier</li>
</ul>

Flickr has already used machine tags, informally, on a couple of occasions:

<ul>
<li>When we launched Maps, we provided a way for people who had
"geotagged" their photos to import their location data. This was
done using the "geo:lat=..." and "geo:lon=..." tags.</li>

<li>When a user tags an event with an upcoming ID (for example :
"upcoming:event=81334") we display a link back to the upcoming.org
site. A similar example is the excellent "Flickr Upcoming Event"
greasemonkey script:<p>

{@link <a href="http://userscripts.org/scripts/show/5464">http://userscripts.org/scripts/show/5464</a>}<p>

Dan Catt wrote a very good piece about machine tags - he called them "triple tags" - last year:<p>

{@link <a href="http://geobloggers.com/archives/2006/01/11/advanced-tagging-and-tripletags/">http://geobloggers.com/archives/2006/01/11/advanced-tagging-and-tripletags/</a>}<p>

Update : Dan's gone and written another excellent piece about all of this stuff now that we've launched machine tags:

{@link <a href="http://geobloggers.com/archives/2007/01/24/offtopic-ish-flickr-ramps-up-triple-tag-support/">http://geobloggers.com/archives/2007/01/24/offtopic-ish-flickr-ramps-up-triple-tag-support/</a>}
</li>
</ul>
<p>

<h3>What is the spec for machine tags?</h3>

Machine tags are divided in to three parts:

<ol>
<li>A "namespace":<p>

Namespaces MUST begin with any character between a - z; remaining
characters MAY be a - z, 0 - 9 and underbars. Namespaces are
case-insensitive.</li>

<li>A "predicate":<p>

Predicates MUST begin with any character between a - z; remaining
characters MAY be a - z, 0 - 9 and underbars. Namespaces are
case-insensitive.</li>

<li>A "value":<p>

Values MAY contain any characters that a "plain vanilla" tags
use. Values may also contain spaces but, like regular tags, they
need to wrapped in quotes.</li>
</ol>

Namespace and predicates are separated by a colon : ":"<p>

Predicates and values are separated by an equals symbol : "="<p>

For example:<p>

<ul>
    <li>flickr:user=straup</li>
    <li>geo:locality="san francisco"</li>
</ul>
<p>

<h3>Why can't I use non-ASCII characters for namespaces and predicates?</h3>

Simple steps, first.<p>

<h3>Can I define another machine tag as the value of a machine tag?</h3>

Sure, but it will not be processed as a machine tag itself.

<h3>How do I add machine tags?</h3>

By adding tags! No, really.

Machine tags are added *exactly* the same as any other tag whether it is done through the website or the API.

When the Flickr supercomputer processes your tags, we take a moment to check
whether it is a machine tag. If it is we leverage the powerful Do What I Mean engine to, well, do what you mean.

<h3>How do I query machine tags?</h3>

Via the API!<p>

Specifically, using the "machinetags" parameter in the 'flickr.photos.search' method. Like tags, you can specify multiple machine tags as a comma separated list.


<h3>Can I query the various part of a machine tag?</h3>

Yes. Aside from passing in a fully formed machine tag, there is a special syntax for searching on specific properties :

<ul>
<li>Find photos using the 'dc' namespace:<p>

{"machine_tags" => "dc:"}</li>

<li>Find photos with a title in the 'dc' namespace:<p>

{"machine_tags" => "dc:title="}</li>

<li>Find photos titled "mr. camera" in the 'dc' namespace:<p>

{"machine_tags" => "dc:title=\"mr. camera\"}</li>

<li>Find photos whose value is "mr. camera":<p>

{"machine_tags" => "*:*=\"mr. camera\""}</li>

<li>Find photos that have a title, in any namespace:<p>

{"machine_tags" => "*:title="}</li>

<li>Find photos that have a title, in any namespace, whose value is "mr. camera":<p>

{"machine_tags" => "*:title=\"mr. camera\""}</li>

<li>Find photos, in the 'dc' namespace whose value is "mr. camera":<p>

{"machine_tags" => "dc:*=\"mr. camera\""}</li>
</ul>

<h3>Is there a limit to the number of machine tags I can query?</h3>

Yes. The limit depends on the tag mode (ALL or ANY) that you are querying
with. "ALL" (AND) queries are limited to (16) machine tags. "ANY" (OR) queries are limited to (8).

<h3>Can I do range queries on machine tags?</h3>

No. Not yet, anyway.<p>

It is a hard problem for reasons far too dull to get in to here. It's on the list.

<h3>Are machine tag namespaces reserved?</h3>

No. Anyone can use a namespace for anything they want.<p>

If you are concerned about colliding namespaces you should consider adding an additional machine tag to define your namespace. For example:<p>

<pre>dc:subject=tags
xmlns:dc=http://purl.org/dc/elements/1.1/</pre>

Like tags, in general, we expect (hope?) that the community will develop its own
standards by consensus over time.

<h3>What about all the machine tags that are already in the Flickr database?</h3>

At the moment, they are still treated as plain old tags.<p>

We have plans to go back and re-import them as machine tags but for now, only new tags will be processed as machine tags.<p>

In the meantime, if you re-save a machine from the 'edit this tag' page it will be re-imported as a machine tag.

<h3>Is the predicate *really* a predicate?</h3>

You are in a dark cave. In the corner is a fire and a man making bunny shadows on the wall with his hands. Whether or not it's really a 'predicate' depends on how much time you spend on the semantic-web mailing list. ;-)<p>

It's close enough to being a predicate that it makes for a good short-hand.

<h3>Wait, aren't machine tags just RDF?</h3>

No, machine tags are not RDF; they could play RDF on television, though.<p>

See also:

{@link <a href="http://weblog.scifihifi.com/2005/08/05/meta-tags-the-poor-mans-rdf">http://weblog.scifihifi.com/2005/08/05/meta-tags-the-poor-mans-rdf</a>}

<h3>Huh, what is RDF?</h3>

RDF Describes Flickr. That's really all you need to know about RDF. 

 * @author mago
 * @version $Id: MachinetagsInterface.java,v 1.3 2009/07/11 20:30:27 x-mago Exp $
 * @see <a href="http://code.flickr.com/blog/2008/07/18/wildcard-machine-tag-urls/">http://code.flickr.com/blog/2008/07/18/wildcard-machine-tag-urls/</a>
 * @see <a href="http://code.flickr.com/blog/2008/08/28/machine-tags-lastfm-and-rocknroll/">http://code.flickr.com/blog/2008/08/28/machine-tags-lastfm-and-rocknroll/</a>
 * @see <a href="http://blech.vox.com/library/post/flickr-exif-machine-tags.html">http://blech.vox.com/library/post/flickr-exif-machine-tags.html</a>
 * @see <a href="http://husk.org/code/machine-tag-browser.html">machine-tag-browser</a>
 */
public class MachinetagsInterface {
    private static final String METHOD_GET_NAMESPACES = "flickr.machinetags.getNamespaces";
    private static final String METHOD_GET_PAIRS = "flickr.machinetags.getPairs";
    private static final String METHOD_GET_PREDICATES = "flickr.machinetags.getPredicates";
    private static final String METHOD_GET_VALUES = "flickr.machinetags.getValues";
    private static final String METHOD_GET_RECENTVALUES = "flickr.machinetags.getRecentValues";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public MachinetagsInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Return a list of unique namespaces, optionally limited by a given
     * predicate, in alphabetical order.
     *
     * This method does not require authentication.
     *
     * @param predicate
     * @param perPage
     * @param page
     * @return NamespacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public NamespacesList getNamespaces(String predicate, int perPage, int page)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        NamespacesList nsList = new NamespacesList();
        parameters.add(new Parameter("method", METHOD_GET_NAMESPACES));
        parameters.add(new Parameter("api_key", apiKey));

        if (predicate != null) {
            parameters.add(new Parameter("predicate", predicate));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject nsElement = response.getData().getJSONObject("namespaces");
        JSONArray nsNodes = nsElement.getJSONArray("namespace");
        nsList.setPage(1);
        nsList.setPages(1);
        nsList.setPerPage(nsNodes.length());
        nsList.setTotal(nsNodes.length());
        for (int i = 0; i < nsNodes.length(); i++) {
        	JSONObject element = nsNodes.getJSONObject(i);
            nsList.add(parseNamespace(element));
        }
        return nsList;
    }

    /**
     * Return a list of unique namespace and predicate pairs,
     * optionally limited by predicate or namespace, in alphabetical order.
     *
     * This method does not require authentication.
     *
     * @param namespace optional, can be null
     * @param predicate optional, can be null
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return NamespacesList containing Pair-objects
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public NamespacesList getPairs(String namespace, String predicate, int perPage, int page)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        NamespacesList nsList = new NamespacesList();
        parameters.add(new Parameter("method", METHOD_GET_PAIRS));
        parameters.add(new Parameter("api_key", apiKey));

        if (namespace != null) {
            parameters.add(new Parameter("namespace", namespace));
        }
        if (predicate != null) {
            parameters.add(new Parameter("predicate", predicate));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject nsElement = response.getData().getJSONObject("pairs");
        JSONArray nsNodes = nsElement.getJSONArray("pair");
        nsList.setPage(nsElement.getInt("page"));
        nsList.setPages(nsElement.getInt("pages"));
        nsList.setPerPage(nsElement.getInt("perPage"));
        nsList.setTotal(nsNodes.length());
        for (int i = 0; i < nsNodes.length(); i++) {
        	JSONObject element = nsNodes.getJSONObject(i);
        	nsList.add(parsePair(element));
        }
        return nsList;
    }

    /**
     * Return a list of unique predicates,
     * optionally limited by a given namespace.
     *
     * This method does not require authentication.
     *
     * @param namespace The namespace that all values should be restricted to.
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return NamespacesList containing Predicate
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public NamespacesList getPredicates(String namespace, int perPage, int page)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        NamespacesList nsList = new NamespacesList();
        parameters.add(new Parameter("method", METHOD_GET_PREDICATES));
        parameters.add(new Parameter("api_key", apiKey));

        if (namespace != null) {
            parameters.add(new Parameter("namespace", namespace));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject nsElement = response.getData().getJSONObject("predicates");
        JSONArray nsNodes = nsElement.getJSONArray("predicate");
        nsList.setPage(nsElement.getInt("page"));
        nsList.setPages(nsElement.getInt("pages"));
        nsList.setPerPage(nsElement.getInt("perPage"));
        nsList.setTotal(nsNodes.length());
        for (int i = 0; i < nsNodes.length(); i++) {
        	JSONObject element = nsNodes.getJSONObject(i);
        	nsList.add(parsePredicate(element));
        }
        return nsList;
    }

    /**
     * Return a list of unique values for a namespace and predicate.
     *
     * This method does not require authentication.
     *
     * @param namespace The namespace that all values should be restricted to.
     * @param predicate The predicate that all values should be restricted to.
     * @param perPage The number of photos to show per page
     * @param page The page offset
     * @return NamespacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public NamespacesList getValues(String namespace, String predicate, int perPage, int page)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        NamespacesList valuesList = new NamespacesList();
        parameters.add(new Parameter("method", METHOD_GET_VALUES));
        parameters.add(new Parameter("api_key", apiKey));

        if (namespace != null) {
            parameters.add(new Parameter("namespace", namespace));
        }
        if (predicate != null) {
            parameters.add(new Parameter("predicate", predicate));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject nsElement = response.getData().getJSONObject("values");
        JSONArray nsNodes = nsElement.getJSONArray("value");
        valuesList.setPage(nsElement.getInt("page"));
        valuesList.setPages(nsElement.getInt("pages"));
        valuesList.setPerPage(nsElement.getInt("perPage"));
        valuesList.setTotal(nsNodes.length());
        for (int i = 0; i < nsNodes.length(); i++) {
        	JSONObject element = nsNodes.getJSONObject(i);
            valuesList.add(parseValue(element));
        }
        return valuesList;
    }

    /**
     * Fetch recently used (or created) machine tags values.
     *
     * This method does not require authentication.
     *
     * @param namespace The namespace that all values should be restricted to.
     * @param predicate The predicate that all values should be restricted to.
     * @param addedSince Only return machine tags values that have been added since this Date. 
     * @return NamespacesList
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     */
    public NamespacesList getRecentValues(String namespace, String predicate, Date addedSince)
      throws FlickrException, IOException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        NamespacesList valuesList = new NamespacesList();
        parameters.add(new Parameter("method", METHOD_GET_RECENTVALUES));
        parameters.add(new Parameter("api_key", apiKey));

        if (namespace != null) {
            parameters.add(new Parameter("namespace", namespace));
        }
        if (predicate != null) {
            parameters.add(new Parameter("predicate", predicate));
        }
        if (addedSince != null) {
            parameters.add(new Parameter(
                "added_since",
                new Long(addedSince.getTime() / 1000L))
            );
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject nsElement = response.getData().getJSONObject("values");
        JSONArray nsNodes = nsElement.getJSONArray("value");
        valuesList.setPage(nsElement.getInt("page"));
        valuesList.setPages(nsElement.getInt("pages"));
        valuesList.setPerPage(nsElement.getInt("perPage"));
        valuesList.setTotal(nsNodes.length());
        for (int i = 0; i < nsNodes.length(); i++) {
        	JSONObject element = nsNodes.getJSONObject(i);
            valuesList.add(parseValue(element));
        }
        return valuesList;
    }

    private Value parseValue(JSONObject nsElement) throws JSONException {
        Value value = new Value();
        value.setUsage(nsElement.getString("usage"));
        value.setNamespace(nsElement.getString("namespace"));
        value.setPredicate(nsElement.getString("predicate"));
        value.setFirstAdded(nsElement.getString("first_added"));
        value.setLastAdded(nsElement.getString("last_added"));
        value.setValue(nsElement.getString("_content"));
        return value;
    }
    
    
    private Predicate parsePredicate(JSONObject nsElement) throws JSONException {
        Predicate predicate = new Predicate();
        predicate.setUsage(nsElement.getString("usage"));
        predicate.setNamespaces(nsElement.getString("namespaces"));
        predicate.setValue(nsElement.getString("_content"));
        return predicate;
    }

    private Namespace parseNamespace(JSONObject nsElement) throws JSONException {
        Namespace ns = new Namespace();
        ns.setUsage(nsElement.getString("usage"));
        ns.setPredicates(nsElement.getString("predicates"));
        ns.setValue(nsElement.getString("_content"));
        return ns;
    }

    private Pair parsePair(JSONObject nsElement) throws JSONException {
        Pair pair = new Pair();
        pair.setUsage(nsElement.getString("usage"));
        pair.setNamespace(nsElement.getString("namespace"));
        pair.setPredicate(nsElement.getString("predicate"));
        return pair;
    }
}
