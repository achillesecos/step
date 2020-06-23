// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long duration = request.getDuration();
    Collection<String> reqAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    Collection<String> allAttendees = new ArrayList<String>();  
    allAttendees.addAll(reqAttendees);
    allAttendees.addAll(optionalAttendees);

    if (!reqAttendees.isEmpty() && queryHelper(events, allAttendees, duration).isEmpty()) {
      return queryHelper(events, reqAttendees, duration);
    }
    return queryHelper(events, allAttendees, duration);
  }

  /* Helper function that returns all possible times for when a meeting could 
     take place given meeting info */
  public Collection<TimeRange> queryHelper(Collection<Event> events, Collection<String> attendees, long duration) {
    // Get TimeRanges of when attendees are busy
    List<TimeRange> busyTimes = new ArrayList<>();
    for (Event event: events) {

      // check any attendee from event is also in attendees
      if (eventHasAttendees(event, attendees)) {
        busyTimes.add(event.getWhen());
      }
    }

    Collections.sort(busyTimes, TimeRange.ORDER_BY_START);

    // Consider edge cases for empty busyTimes
    if (busyTimes.isEmpty() && (TimeRange.WHOLE_DAY.duration() >= duration)) {
      busyTimes.add(TimeRange.WHOLE_DAY);
      return busyTimes;
    } else if (busyTimes.isEmpty()) {
      return busyTimes;
    }

    List<TimeRange> mergedTimes = mergeBusyTimes(busyTimes);

    return getValidTimes(mergedTimes, duration);
  }

  /* Will merge all the overlapping times that are busy meeting times */
  public List<TimeRange> mergeBusyTimes(List<TimeRange> busyTimes) {
    List<TimeRange> mergedTimes = new ArrayList<TimeRange>();
    mergedTimes.add(busyTimes.get(0));

    for (int i = 1; i < busyTimes.size(); i++) {
      TimeRange prevTime = busyTimes.get(i-1);
      TimeRange currTime = busyTimes.get(i);
      
      if (prevTime.overlaps(currTime)) {
          int newStart = Math.min(prevTime.start(), currTime.start());
          int newEnd = Math.max(prevTime.end(), currTime.end());
          int newDuration = newEnd - newStart;

          // Remove both events that overlapped, add the merged one
          busyTimes.remove(i);
          busyTimes.remove(i-1);
          busyTimes.add(i-1, TimeRange.fromStartDuration(newStart, newDuration));
          // Offset index
          i--;
      }
    }
    return busyTimes;
  }

  /* Returns the valid meeting time from the merged meeting times, given the
     requested meeting duration */
  public Collection<TimeRange> getValidTimes(List<TimeRange> mergedTimes, long duration) {
    ArrayList<TimeRange> validTimes = new ArrayList<TimeRange>();
    int startOfFirst = mergedTimes.get(0).start();
    int firstDur = startOfFirst - TimeRange.START_OF_DAY;
    if (firstDur != 0){
      validTimes.add(TimeRange.fromStartDuration(TimeRange.START_OF_DAY, firstDur));
    }

    for (int i = 1; i < mergedTimes.size(); i++) {
      int endOfPrev = mergedTimes.get(i-1).end();
      int startOfCurr = mergedTimes.get(i).start();
      int timeDiff = startOfCurr - endOfPrev;
      
      // if the potential new duration is not shorter than duration we are requesting, add it
      if(timeDiff >= duration) {
        validTimes.add(TimeRange.fromStartDuration(endOfPrev, timeDiff));
      }
    }

    int startOfLast = mergedTimes.get(mergedTimes.size()-1).end();
    int lastDur = TimeRange.END_OF_DAY - startOfLast + 1;

    if (lastDur != 0) {
      validTimes.add(TimeRange.fromStartDuration(startOfLast, lastDur));
    }

    return validTimes;
  }

  /* Check if event has any attendee from attendees */
  public Boolean eventHasAttendees(Event event, Collection<String> attendees) {
    for (String attendee : event.getAttendees()) {
      if (attendees.contains(attendee)) return true;
    }
    return false;
  }

}