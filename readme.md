Hi!

Please take a couple minutes to read those notes below. Thx!
Use following command line to launch an app:
 
_java -jar belkaTest.jar in.txt out.txt_

Jar-file can be found in _out/artifacts_ dir

**Note 1.**

As far as I understand there are possible bottlenecks during multi-thread reading of file
especially when file placed on one single classic hard-drive (not SSD, nor RAID), in this 
particular case sequential (single thread) reading will be faster than random reading because of 
hardware limits. 

**Note 2.**

There is no requirement to launch all threads at once (such functionality can be implemented 
using CountDownLatch), so for simplicity purpose I will launch them using thread pool.

**Note 3.** 

Test-data contains natural row number from 1 to 1000, then starts again from 1, used only to 
check correctness of algorithm, not used in anyway inside program logic. It is easy to check 
consistency according to specified requirements in any file editor app with "Show line number" 
support.

**ALSO, PLEASE BE AWARE**, there is a empty line between first thousand lines and second. Feel free
to use any other multiline text file as source of data

**Note 4.**

"Death Pill" approach used to control thread lifecycle. So thread starts his life, and begins 
shutting down process (or dies immediately) on special object encounter. In this particular 
implementation, processing object with Line-Number = -1, serves as "Death Pill". 

**Note 5.**

There is no validations for such things as gigantic single line file. I assume that file is 
LOG-like, separated by UNIX\WINDOWS line delimiters.

**Note 6.**

Threads will write some info (on start and on shutdown) in default stdout.

**Note 7.**

Work diagram: Input File -> InputQueue ===(Processing)===> OutputQueue -> Output File 
According to requirements, only 10 lines will be stored in input queue. There is no limitations
for output queue.

**Note 8.**

