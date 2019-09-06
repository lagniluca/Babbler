/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.babbler.ws.io.avro.model;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
/** Avro schema used for representing a babble on Kafka */
@org.apache.avro.specific.AvroGenerated
public class BabbleKey extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7591568608114787830L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BabbleKey\",\"namespace\":\"com.babbler.ws.io.avro.model\",\"doc\":\"Avro schema used for representing a babble on Kafka\",\"fields\":[{\"name\":\"location\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"location of the babble\"},{\"name\":\"tags\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"default\":[]},{\"name\":\"mentions\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"default\":[]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<BabbleKey> ENCODER =
      new BinaryMessageEncoder<BabbleKey>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<BabbleKey> DECODER =
      new BinaryMessageDecoder<BabbleKey>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<BabbleKey> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<BabbleKey> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<BabbleKey>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this BabbleKey to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a BabbleKey from a ByteBuffer. */
  public static BabbleKey fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** location of the babble */
   private java.lang.String location;
   private java.util.List<java.lang.String> tags;
   private java.util.List<java.lang.String> mentions;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public BabbleKey() {}

  /**
   * All-args constructor.
   * @param location location of the babble
   * @param tags The new value for tags
   * @param mentions The new value for mentions
   */
  public BabbleKey(java.lang.String location, java.util.List<java.lang.String> tags, java.util.List<java.lang.String> mentions) {
    this.location = location;
    this.tags = tags;
    this.mentions = mentions;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return location;
    case 1: return tags;
    case 2: return mentions;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: location = (java.lang.String)value$; break;
    case 1: tags = (java.util.List<java.lang.String>)value$; break;
    case 2: mentions = (java.util.List<java.lang.String>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'location' field.
   * @return location of the babble
   */
  public java.lang.String getLocation() {
    return location;
  }


  /**
   * Gets the value of the 'tags' field.
   * @return The value of the 'tags' field.
   */
  public java.util.List<java.lang.String> getTags() {
    return tags;
  }


  /**
   * Gets the value of the 'mentions' field.
   * @return The value of the 'mentions' field.
   */
  public java.util.List<java.lang.String> getMentions() {
    return mentions;
  }


  /**
   * Creates a new BabbleKey RecordBuilder.
   * @return A new BabbleKey RecordBuilder
   */
  public static com.babbler.ws.io.avro.model.BabbleKey.Builder newBuilder() {
    return new com.babbler.ws.io.avro.model.BabbleKey.Builder();
  }

  /**
   * Creates a new BabbleKey RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new BabbleKey RecordBuilder
   */
  public static com.babbler.ws.io.avro.model.BabbleKey.Builder newBuilder(com.babbler.ws.io.avro.model.BabbleKey.Builder other) {
    return new com.babbler.ws.io.avro.model.BabbleKey.Builder(other);
  }

  /**
   * Creates a new BabbleKey RecordBuilder by copying an existing BabbleKey instance.
   * @param other The existing instance to copy.
   * @return A new BabbleKey RecordBuilder
   */
  public static com.babbler.ws.io.avro.model.BabbleKey.Builder newBuilder(com.babbler.ws.io.avro.model.BabbleKey other) {
    return new com.babbler.ws.io.avro.model.BabbleKey.Builder(other);
  }

  /**
   * RecordBuilder for BabbleKey instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<BabbleKey>
    implements org.apache.avro.data.RecordBuilder<BabbleKey> {

    /** location of the babble */
    private java.lang.String location;
    private java.util.List<java.lang.String> tags;
    private java.util.List<java.lang.String> mentions;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.babbler.ws.io.avro.model.BabbleKey.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.location)) {
        this.location = data().deepCopy(fields()[0].schema(), other.location);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.tags)) {
        this.tags = data().deepCopy(fields()[1].schema(), other.tags);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.mentions)) {
        this.mentions = data().deepCopy(fields()[2].schema(), other.mentions);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing BabbleKey instance
     * @param other The existing instance to copy.
     */
    private Builder(com.babbler.ws.io.avro.model.BabbleKey other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.location)) {
        this.location = data().deepCopy(fields()[0].schema(), other.location);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.tags)) {
        this.tags = data().deepCopy(fields()[1].schema(), other.tags);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.mentions)) {
        this.mentions = data().deepCopy(fields()[2].schema(), other.mentions);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'location' field.
      * location of the babble
      * @return The value.
      */
    public java.lang.String getLocation() {
      return location;
    }

    /**
      * Sets the value of the 'location' field.
      * location of the babble
      * @param value The value of 'location'.
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder setLocation(java.lang.String value) {
      validate(fields()[0], value);
      this.location = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'location' field has been set.
      * location of the babble
      * @return True if the 'location' field has been set, false otherwise.
      */
    public boolean hasLocation() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'location' field.
      * location of the babble
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder clearLocation() {
      location = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'tags' field.
      * @return The value.
      */
    public java.util.List<java.lang.String> getTags() {
      return tags;
    }

    /**
      * Sets the value of the 'tags' field.
      * @param value The value of 'tags'.
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder setTags(java.util.List<java.lang.String> value) {
      validate(fields()[1], value);
      this.tags = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'tags' field has been set.
      * @return True if the 'tags' field has been set, false otherwise.
      */
    public boolean hasTags() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'tags' field.
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder clearTags() {
      tags = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'mentions' field.
      * @return The value.
      */
    public java.util.List<java.lang.String> getMentions() {
      return mentions;
    }

    /**
      * Sets the value of the 'mentions' field.
      * @param value The value of 'mentions'.
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder setMentions(java.util.List<java.lang.String> value) {
      validate(fields()[2], value);
      this.mentions = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'mentions' field has been set.
      * @return True if the 'mentions' field has been set, false otherwise.
      */
    public boolean hasMentions() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'mentions' field.
      * @return This builder.
      */
    public com.babbler.ws.io.avro.model.BabbleKey.Builder clearMentions() {
      mentions = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BabbleKey build() {
      try {
        BabbleKey record = new BabbleKey();
        record.location = fieldSetFlags()[0] ? this.location : (java.lang.String) defaultValue(fields()[0]);
        record.tags = fieldSetFlags()[1] ? this.tags : (java.util.List<java.lang.String>) defaultValue(fields()[1]);
        record.mentions = fieldSetFlags()[2] ? this.mentions : (java.util.List<java.lang.String>) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<BabbleKey>
    WRITER$ = (org.apache.avro.io.DatumWriter<BabbleKey>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<BabbleKey>
    READER$ = (org.apache.avro.io.DatumReader<BabbleKey>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
