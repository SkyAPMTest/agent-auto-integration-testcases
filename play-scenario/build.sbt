name := """play-scenario"""
organization := "org.apache.skywalking"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice

maintainer := "yanbo.ai@gmail.com"
