elaUtils
========

elaUtils is a set of Java utilities classes that are used by ELA solutions.  The classes that do not fit into other third party libs have been brought into elaUtils.

## Usage

**CloseUtil** - Provides a central place for close behavior to reduce copy & pasting.

```
CloseUtil.close(fileHandle);
```
<br/>
**Copy** - Utility for making object copies without the need for adding cloning to an object.  Copy does a deep copy.<br/>
Note: There is no need for casting.

```
final Car firstCar = new Car("Ford");
final Car copyCar = Copy.copy(firstCar);
```
<br/>

<br/>
**StringDoubleUtil** - Converts Strings to doubles.

<br/>
**SimpleCache<I, T>** - An simple alternative to ehcache.

```
String cacheName = "simple";
long expire = 1000; // the minimum amount of time in milliseconds that the object should stay in cache
long period = 60000; // period time in milliseconds between successive expire checks.

m_store = new SimpleCache<Integer, Car>(CacheTemplateDemo.class.getName(), 4000, period);
....
final Integer key = Integer.valueOf(1);
m_store.put(key, new Car(key, "my car"));

final Integer key2 = Integer.valueOf(2);
m_store.put(key2, new Car(key2, "van"));

Car car = m_store.get(key);
System.out.println(car.m_name);

m_store.put(key, new Car(key, "my car update"));
car = m_store.get(key);
System.out.println(car.m_name);

try { Thread.sleep(10000); } catch (final InterruptedException excep) { }

m_store.put(key, new Car(key, "my car update again"));
car = m_store.get(key);
System.out.println(car.m_name);

// careful, due to timeout data may not be returned
car = m_store.get(key2);
if( car!=null) {
	System.out.println(car.m_name);
} else {
	System.out.println("No car corresponding to " + key2.toString());
}
<br/>
```

**CryptoService** Wraps to encrypt/decrypt functionality.

```
int iterationCount = 19;

String passPhrase = "YourPassPhrase";

// 8-byte Salt
byte[] salt ={ 
	(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
	(byte) 0x35, (byte) 0xE3, (byte) 0x03 
};

Crypto encrypter = CryptoService.newUtils(passPhrase, salt, iterationCount);
final String encrypted = encrypter.encrypt("Don't tell anybody!");
System.out.println("encrypted=" + encrypted); 					// encrypted=wvE4cZuDblPZpKs78bVTZHroAB7ouFGh
System.out.println("decrypted="+encrypter.decrypt(encrypted));	// decrypted=Don't tell anybody!
```
<b>Output</b>
```
encrypted=wvE4cZuDblPZpKs78bVTZHroAB7ouFGh
decrypted=Don't tell anybody!
```		

<br/>
**SyncWriter** - An interface and set of concreate classes that controls the writing to a file from multiple threads.

<br/>
**HessianProxy** - Singleton wrapper to the HessianProxyFactory.

<br/>
**ServletUtil** - Dumps the HttpServletRequest to a buffer for display, debugging or logging.

<br/>
**WebClient** - A mechanism to detect client software, version and platform from a user-agent string.

<br/>
**StressTest** - Java based stress test service.  Write the stress test in Java rather than using an elaborate GUI tools.

<br/>
**FieldTypes** - Reusable type information in the form of a enumeration with methods.

```
FieldTypes.DOUBLE
```
