// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/ControlMessage.proto

package org.apache.hudi.connect;

public final class ConnectControl {
  private ConnectControl() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_connect_ControlMessage_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_connect_ControlMessage_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_connect_ControlMessage_CoordinatorInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_connect_ControlMessage_CoordinatorInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_connect_ControlMessage_CoordinatorInfo_GlobalKafkaCommitOffsetsEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_connect_ControlMessage_CoordinatorInfo_GlobalKafkaCommitOffsetsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_connect_ControlMessage_ParticipantInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_connect_ControlMessage_ParticipantInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_connect_ControlMessage_ConnectWriteStatus_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_connect_ControlMessage_ConnectWriteStatus_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\'src/main/resources/ControlMessage.prot" +
      "o\022\007connect\"\236\007\n\016ControlMessage\022\027\n\017protoco" +
      "lVersion\030\001 \001(\r\022/\n\004type\030\002 \001(\0162!.connect.C" +
      "ontrolMessage.EventType\022\022\n\ntopic_name\030\003 " +
      "\001(\t\0227\n\013sender_type\030\004 \001(\0162\".connect.Contr" +
      "olMessage.EntityType\022\030\n\020sender_partition" +
      "\030\005 \001(\r\0229\n\rreceiver_type\030\006 \001(\0162\".connect." +
      "ControlMessage.EntityType\022\032\n\022receiver_pa" +
      "rtition\030\007 \001(\r\022\022\n\ncommitTime\030\010 \001(\t\022C\n\020coo" +
      "rdinator_info\030\t \001(\0132\'.connect.ControlMes" +
      "sage.CoordinatorInfoH\000\022C\n\020participant_in" +
      "fo\030\n \001(\0132\'.connect.ControlMessage.Partic" +
      "ipantInfoH\000\032\273\001\n\017CoordinatorInfo\022g\n\030globa" +
      "lKafkaCommitOffsets\030\001 \003(\0132E.connect.Cont" +
      "rolMessage.CoordinatorInfo.GlobalKafkaCo" +
      "mmitOffsetsEntry\032?\n\035GlobalKafkaCommitOff" +
      "setsEntry\022\013\n\003key\030\001 \001(\005\022\r\n\005value\030\002 \001(\003:\0028" +
      "\001\032g\n\017ParticipantInfo\022?\n\013writeStatus\030\001 \001(" +
      "\0132*.connect.ControlMessage.ConnectWriteS" +
      "tatus\022\023\n\013kafkaOffset\030\002 \001(\004\0323\n\022ConnectWri" +
      "teStatus\022\035\n\025serializedWriteStatus\030\001 \001(\014\"" +
      "O\n\tEventType\022\020\n\014START_COMMIT\020\000\022\016\n\nEND_CO" +
      "MMIT\020\001\022\016\n\nACK_COMMIT\020\002\022\020\n\014WRITE_STATUS\020\003" +
      "\".\n\nEntityType\022\017\n\013COORDINATOR\020\000\022\017\n\013PARTI" +
      "CIPANT\020\001B\t\n\007payloadB+\n\027org.apache.hudi.c" +
      "onnectB\016ConnectControlP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_connect_ControlMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_connect_ControlMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_connect_ControlMessage_descriptor,
        new java.lang.String[] { "ProtocolVersion", "Type", "TopicName", "SenderType", "SenderPartition", "ReceiverType", "ReceiverPartition", "CommitTime", "CoordinatorInfo", "ParticipantInfo", "Payload", });
    internal_static_connect_ControlMessage_CoordinatorInfo_descriptor =
      internal_static_connect_ControlMessage_descriptor.getNestedTypes().get(0);
    internal_static_connect_ControlMessage_CoordinatorInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_connect_ControlMessage_CoordinatorInfo_descriptor,
        new java.lang.String[] { "GlobalKafkaCommitOffsets", });
    internal_static_connect_ControlMessage_CoordinatorInfo_GlobalKafkaCommitOffsetsEntry_descriptor =
      internal_static_connect_ControlMessage_CoordinatorInfo_descriptor.getNestedTypes().get(0);
    internal_static_connect_ControlMessage_CoordinatorInfo_GlobalKafkaCommitOffsetsEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_connect_ControlMessage_CoordinatorInfo_GlobalKafkaCommitOffsetsEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_connect_ControlMessage_ParticipantInfo_descriptor =
      internal_static_connect_ControlMessage_descriptor.getNestedTypes().get(1);
    internal_static_connect_ControlMessage_ParticipantInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_connect_ControlMessage_ParticipantInfo_descriptor,
        new java.lang.String[] { "WriteStatus", "KafkaOffset", });
    internal_static_connect_ControlMessage_ConnectWriteStatus_descriptor =
      internal_static_connect_ControlMessage_descriptor.getNestedTypes().get(2);
    internal_static_connect_ControlMessage_ConnectWriteStatus_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_connect_ControlMessage_ConnectWriteStatus_descriptor,
        new java.lang.String[] { "SerializedWriteStatus", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
