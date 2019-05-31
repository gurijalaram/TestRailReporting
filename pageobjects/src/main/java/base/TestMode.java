package main.java.base;

public enum TestMode {
 QA, // test against env that QA uses like cid-te
 LOCAL, // test locally, default
 EXPORT // run export tests, handy when running over RemoteWebDriver as download will happen on remote machine
}
