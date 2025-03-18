// package org.asansocketserver.domain.watch.dto.web.response;

// import lombok.AccessLevel;
// import lombok.Builder;
// import org.asansocketserver.domain.watch.entity.Watch;
// import org.asansocketserver.domain.watch.entity.WatchCoordinateProhibition;
// import org.asansocketserver.domain.watch.entity.WatchNoContact;

// import java.util.List;
// import java.util.stream.Collectors;

// @Builder(access = AccessLevel.PRIVATE)
// public record WatchResponseForWebDto(
// Long watchId,
// String name,
// String host,
// String gender,
// String highRisk,
// int minHr,
// int maxHr,
// List<Long> prohibitedCoordinateList,
// List<Long> noContactPatientList
// ) {
// public static WatchResponseForWebDto of(Watch watch) {
// return WatchResponseForWebDto.builder()
// .watchId(watch.getId())
// .name(watch.getName())
// .host(watch.getHost())
// .gender(watch.getGender().toString())
// .highRisk(watch.getHighRisk().toString())
// .minHr(watch.getMinHR())
// .maxHr(watch.getMaxHR())
// .prohibitedCoordinateList(watch.getProhibitedCoordinateList().stream()
// .map(prohibition -> prohibition.getCoordinate().getId())
// .collect(Collectors.toList()))

// .noContactPatientList(watch.getNoContactWatchList().stream()
// .map(watchNoContact -> watchNoContact.getNoContactWatch().getId())
// .collect(Collectors.toList()))
// .build();
// }

// public static List<WatchResponseForWebDto> listOf(List<Watch> watchList) {
// return watchList.stream()
// .map(WatchResponseForWebDto::of)
// .collect(Collectors.toList());
// }
// }
