package com.example.hugo;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import hugo.weaving.anotations.DebugGenericLog;

public class HugoActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_hugo);

    findViewById(R.id.activity_hugo_test_generic_annotation_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        printArgs("The", "Quick", "Brown", "Fox");

        Log.i("Fibonacci", "fibonacci's 4th number is " + fibonacci(4));

        Greeter greeter = new Greeter("Jake");
        Log.d("Greeting", greeter.sayHello());

        Charmer charmer = new Charmer("Jake");
        Log.d("Charming", charmer.askHowAreYou());

        startSleepyThread();
      }
    });
  }

  @DebugGenericLog
  private void printArgs(String... args) {
    for (String arg : args) {
      Log.i("Args", arg);
    }
  }

  @DebugGenericLog
  private int fibonacci(int number) {
    if (number <= 0) {
      throw new IllegalArgumentException("Number must be greater than zero.");
    }
    if (number == 1 || number == 2) {
      return 1;
    }
    // NOTE: Don't ever do this. Use the iterative approach!
    return fibonacci(number - 1) + fibonacci(number - 2);
  }

  private void startSleepyThread() {
    new Thread(new Runnable() {
      private static final long SOME_POINTLESS_AMOUNT_OF_TIME = 50;

      @Override public void run() {
        sleepyMethod(SOME_POINTLESS_AMOUNT_OF_TIME);
      }

      @DebugGenericLog
      private void sleepyMethod(long milliseconds) {
        SystemClock.sleep(milliseconds);
      }
    }, "I'm a lazy thr.. bah! whatever!").start();
  }

  @DebugGenericLog
  static class Greeter {
    private final String name;

    Greeter(String name) {
      this.name = name;
    }

    private String sayHello() {
      return "Hello, " + name;
    }
  }

  @DebugGenericLog
  static class Charmer {
    private final String name;

    private Charmer(String name) {
      this.name = name;
    }

    public String askHowAreYou() {
      return "How are you " + name + "?";
    }
  }
}
